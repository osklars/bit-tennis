package model

import model.api.GameEvent
import upickle.default.*

case class StateHistory
(
  event: GameEvent,
  state: MatchState
) derives ReadWriter
