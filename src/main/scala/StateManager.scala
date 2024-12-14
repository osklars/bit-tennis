import cats.effect.{Concurrent, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic

class StateManager[F[_] : Concurrent]
(
  state: Ref[F, List[GameState]],
  updates: Topic[F, List[GameState]]
):
  def process(event: GameEvent): F[GameState] = for
    states <- state.get
    currentState = states.headOption.getOrElse(GameState.initial(Player.A))
    newState = currentState.process(event)
    newHistory = (newState :: states).take(5) // Keep last 5 states
    _ <- state.set(newHistory)
    _ <- updates.publish1(newHistory)
  yield newState

  def getState: F[GameState] = state.get.map(_.head)

  def subscribe: Stream[F, List[GameState]] = updates.subscribe(10)