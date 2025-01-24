package model.api.in

import model.types.{EventType, Side}
import upickle.default.*

case class Event
(
  event: EventType,
  side: Option[Side] = None,
) derives ReadWriter
