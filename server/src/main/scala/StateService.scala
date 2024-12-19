import cats.effect.{IO, Ref}
import cats.syntax.all.*
import fs2.concurrent.Topic
import fs2.io.file.{Files, Path}
import fs2.{Stream, text}
import model.InternalState
import model.api.in.{DetectionEvent, NewMatch}
import model.api.out.StateSummary
import model.pingis.MatchState
import model.types.ManualInput
import upickle.default.*

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

class StateService
(
  state: Ref[IO, MatchState],
  history: Ref[IO, List[InternalState]],
  updates: Topic[IO, List[StateSummary]]
):
  def newMatch(input: NewMatch): IO[Int] =
    for
      list <- history.get
      _ <- list.lastOption.map(toFile(_, list)).getOrElse(IO.unit)
      _ <- updateHistory(List.empty)
      _ <- state.set(MatchState(input))
    yield list.size

  def process(event: DetectionEvent): IO[StateSummary] =
    for
      list <- history.get
      _ <- IO.println("current", list.map(_.event.timestamp))
      (subsequent, previous) = list.span(event.timestamp < _.event.timestamp)
      newState <- previous.headOption
        .map(_.matchState.process(event))
        .map(InternalState(event, _))
        .liftTo[IO](new Exception("No match in progress"))
      (latestState, recalculated) = subsequent.reverse
        .mapAccumulate(newState)((state, next) => {
          val newState = InternalState(next.event, state.matchState.process(next.event))
          (newState, newState)
        })
      _ <- updateHistory(recalculated ++ (newState :: previous))
      _ <- state.set(latestState.matchState)
    yield StateSummary(latestState)

  private def updateHistory(newHistory: List[InternalState]): IO[List[InternalState]] =
    for
      _ <- history.set(newHistory)
      _ <- updates.publish1(newHistory.take(5).map(StateSummary.apply))
    yield newHistory

  def getLatest(): IO[List[StateSummary]] =
    history
      .get
      .map(_.take(5).map(StateSummary.apply))

  def subscribe: Stream[IO, List[StateSummary]] =
    updates
      .subscribe(10)

  private def toFile(state: InternalState, states: List[InternalState]): IO[Unit] =
    fs2.Stream.emits(states)
      .map(StateSummary.apply)
      .map(write(_))
      .intersperse("\n")
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path(s"${state.matchState.playerA}-${state.matchState.playerB}_${format(state.event.timestamp)}.txt")))
      .compile.drain

  def format(timestamp: Long): String =
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter
      .ofPattern("yyMMddHHmmss")
      .withZone(ZoneId.systemDefault())
    formatter.format(instant)