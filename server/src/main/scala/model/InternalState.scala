package model

import model.api.in.{Event, NewMatch}
import model.pingis.MatchState

case class InternalState
(
  event: Event,
  matchState: MatchState,
)



