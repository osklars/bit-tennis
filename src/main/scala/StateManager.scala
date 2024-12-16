import cats.effect.{IO, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic
import model.api.NewMatch
import model.{GameEvent, MatchState}
import upickle.default.*

case class StateHistory
(
  event: GameEvent,
  state: MatchState
)derives ReadWriter

class StateManager
(
  state: Ref[IO, Option[MatchState]],
  history: Ref[IO, List[StateHistory]],
  updates: Topic[IO, List[StateHistory]]
):
  def newMatch(input: NewMatch) =
    state.set(Some(MatchState.newMatch(input)))

  def process(event: GameEvent): IO[MatchState] =
    for
      current <- state.get
      newState <- current.map(_.process(event)).liftTo[IO](Exception("No ongoing match"))
      _ <- state.set(Some(newState))
      previous <- history.get
      newHistory = StateHistory(event, newState) :: previous
      _ <- history.set(newHistory)
      _ <- updates.publish1(newHistory.take(5))
    yield newState

  def getHistory: IO[List[StateHistory]] = history.get

  def subscribe: Stream[IO, List[StateHistory]] = updates.subscribe(10)