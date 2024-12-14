package com.example

import cats.effect.{Ref, Concurrent}
import cats.syntax.all.*
import fs2.Stream
import fs2.concurrent.Topic

class StateManager[F[_]: Concurrent](
                                    state: Ref[F, GameState],
                                    updates: Topic[F, GameState]
                                  ):
  def process(event: GameEvent): F[GameState] = for
    currentState <- state.get
    newState = currentState.process(event)
    _ <- state.set(newState)
    _ <- updates.publish1(newState)
  yield newState

  def getState: F[GameState] = state.get

  def subscribe: Stream[F, GameState] = updates.subscribe(10)