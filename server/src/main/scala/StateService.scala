import cats.effect.{IO, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic
import model.InternalState
import model.api.in.{DetectionEvent, NewMatch}
import model.api.out.StateSummary
import model.pingis.MatchState
import model.types.ManualInput

class StateService
(
  history: Ref[IO, List[InternalState]],
  updates: Topic[IO, List[StateSummary]]
):
  def newMatch(input: NewMatch): IO[StateSummary] =
    for
      list <- history.get
      (previous, subsequent) = list.span(_.timestamp < input.timestamp)
      firstNew = InternalState(Right(input), MatchState(input), input.timestamp)
      (last, recalculated) = recalculate(subsequent, firstNew)
      _ <- updateHistory(previous ++ (firstNew :: recalculated))
    yield StateSummary(last)

  def process(event: DetectionEvent): IO[StateSummary] =
    for
      list <- history.get
      _ <- IO.println("current", list.map(_.timestamp))
      (previous, subsequent) = list.span(_.timestamp < event.timestamp)
      lastPrevious <- previous.lastOption.liftTo[IO](new Exception("No match in progress"))
      firstNew = lastPrevious.process(Left(event), event.timestamp)
      (last, recalculated) = recalculate(subsequent, firstNew)
      _ <- updateHistory(previous ++ (firstNew :: recalculated))
    yield StateSummary(last)

  private def updateHistory(newHistory: List[InternalState]): IO[List[InternalState]] =
    for
      _ <- history.set(newHistory)
      _ <- updates.publish1(newHistory.take(5).map(StateSummary.apply))
    yield newHistory
    
  private def recalculate(tail: List[InternalState], initial: InternalState): (InternalState, List[InternalState]) =
    tail
      .mapAccumulate(initial)((state, next) => {
        val newState = state.process(next.event, next.timestamp)
        (newState, newState)
      })

  def getLatest(): IO[List[StateSummary]] =
    history
      .get
      .map(_.take(5).map(StateSummary.apply))

  def subscribe: Stream[IO, List[StateSummary]] =
    updates
      .subscribe(10)