package model

import upickle.default.*

case class GameEvent
(
  ballEvent: BallEvent,
  player: Option[Player],
  timestamp: Long
) derives ReadWriter