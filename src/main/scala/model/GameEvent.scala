package model

import upickle.default.ReadWriter

case class GameEvent
(
  ballEvent: BallEvent,
  player: Option[Player],
  timestamp: Long
)derives ReadWriter