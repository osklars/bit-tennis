import cats.effect.{Concurrent, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic
import upickle.default.*

case class GameHistory
(
  event: GameEvent,
  state: GameState
) derives ReadWriter

class StateManager[F[_] : Concurrent]
(
  state: Ref[F, GameState],
  history: Ref[F, List[GameHistory]],
  updates: Topic[F, List[GameHistory]]
):
  def process(event: GameEvent): F[GameState] = for
    current <- state.get
    newState = current.process(event)
    _ <- state.set(newState)
    previous <- history.get
    newHistory = GameHistory(event, newState) :: previous
    _ <- history.set(newHistory)
    _ <- updates.publish1(newHistory.take(5))
  yield newState

  def subscribe: Stream[F, List[GameHistory]] = updates.subscribe(10)