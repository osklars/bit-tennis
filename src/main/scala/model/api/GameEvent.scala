package model.api

import model.Player
import upickle.default.*

sealed trait GameEvent derives ReadWriter

case class DetectionEvent
(
  detection: Detection,
  player: Option[Player],
  timestamp: Long
) extends GameEvent

case class InputEvent
(
  input: ManualInput,
  player: Option[Player],
  timestamp: Long
) extends GameEvent