package model.api.out

import model.api.in.Event
import model.api.out.StateSummary
import upickle.default.*

case class InvalidEvent(event: Event, state: StateSummary) 
  extends Exception(s"Unhandled event: ${write(event)} with state ${write(state)}") 
    derives ReadWriter