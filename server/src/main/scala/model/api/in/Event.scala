package model.api.in

import model.types.{EventType, Player, Side}
import upickle.default.*

case class Event
(
  event: EventType,
  side: Option[Side],
) derives ReadWriter
