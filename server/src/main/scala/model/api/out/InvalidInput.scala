package model.api.out

import model.api.in.Input
import model.api.out.StateSummary
import upickle.default.*

case class InvalidInput(input: Input, state: StateSummary)
  extends Exception(s"Unhandled input: ${write(input)} with state ${write(state)}") 
    derives ReadWriter