import cats.effect.{IO, Ref}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic
import model.InvalidEvent
import model.api.in.{Event, NewMatch}
import model.api.out.StateSummary
import model.pingis.MatchState

class StateService
(
  state: Ref[IO, MatchState],
  updates: Topic[IO, StateSummary]
):
  def newMatch(input: NewMatch): IO[StateSummary] =
    val newState = MatchState(input)
    for
      _ <- state.set(newState)
      summary = StateSummary(newState)
      _ <- updates.publish1(summary)
    yield summary

  def process(event: Event): IO[StateSummary] =
    for
      current <- state.get
      _ <- IO.println("handling event", current, event)
      newState <- current.process(event)
        .liftTo[IO](InvalidEvent(event, StateSummary(current)))
      _ <- IO.println("got new state", newState)
      _ <- state.set(newState)
      summary = StateSummary(event, newState)
      _ <- updates.publish1(summary)
    yield summary

  def getState: IO[StateSummary] = state.get.map(StateSummary.apply)

  def subscribe: Stream[IO, StateSummary] =
    updates
      .subscribeUnbounded