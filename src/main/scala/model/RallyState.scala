package model

import upickle.default.*

enum RallyState derives ReadWriter:
  case Idle, ToServe, ToBounce1, ToBounce2, NetServe // Serve states
  case ToStrike, ToBounce // Return states
  case Error(message: String) 
