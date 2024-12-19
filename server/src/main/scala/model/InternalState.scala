package model

import model.api.in.{DetectionEvent, NewMatch}
import model.pingis.MatchState

case class InternalState
(
  event: DetectionEvent,
  matchState: MatchState,
)



