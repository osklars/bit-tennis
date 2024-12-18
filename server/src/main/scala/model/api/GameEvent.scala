package model.api

import model.Player
import upickle.default.*

case class GameEvent
(
  detection: Option[DetectionEvent],
  input: Option[InputEvent],
) derives ReadWriter

case class DetectionEvent
(
  detection: Detection,
  player: Option[Player],
  timestamp: Long
) derives ReadWriter

case class InputEvent
(
  input: ManualInput,
  player: Option[Player],
  timestamp: Long
) derives ReadWriter