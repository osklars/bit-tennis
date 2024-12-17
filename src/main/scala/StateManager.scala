import cats.effect.{IO, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic
import model.api.{DetectionEvent, GameEvent, InputEvent, ManualInput, NewMatch}
import model.{MatchState, StateHistory}

class StateManager
(
  state: Ref[IO, Option[MatchState]],
  history: Ref[IO, List[StateHistory]],
  updates: Topic[IO, List[StateHistory]]
):
  def newMatch(input: NewMatch): IO[MatchState] =
    val m = MatchState.newMatch(input)
    for
      _ <- state.set(Some(m))
      _ <- updateHistory(InputEvent(ManualInput.NewMatch, None, input.timestamp), m)
    yield m

  def process(event: DetectionEvent): IO[MatchState] =
    for
      current <- state.get.flatMap(_.liftTo[IO](Exception("No ongoing match")))
      newState = current.process(event)
      _ <- state.set(Some(newState))
      _ <- updateHistory(event, newState)
    yield newState
    
  def updateHistory(event: GameEvent, newState: MatchState): IO[List[StateHistory]] =
    for
      previous <- history.get
      newHistory = StateHistory(event, newState) :: previous
      _ <- history.set(newHistory)
      _ <- updates.publish1(newHistory.take(5))
    yield newHistory

  def getHistory: IO[List[StateHistory]] = history.get

  def subscribe: Stream[IO, List[StateHistory]] = updates.subscribe(10)