package model.api.in

import model.types.{EventType, Player}
import upickle.default.*

case class Event
(
  event: EventType,
  player: Option[Player]
) derives ReadWriter
