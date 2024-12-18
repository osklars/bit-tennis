package model.api.in

import model.types.{Detection, Player}
import upickle.default.*

case class DetectionEvent
(
  detection: Detection,
  player: Option[Player],
  timestamp: Long
) derives ReadWriter
