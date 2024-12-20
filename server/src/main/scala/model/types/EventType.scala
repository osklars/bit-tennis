package model.types

import upickle.default.*

enum EventType derives ReadWriter:
  case Throw, ExitTable, Net // only relevant for serves
  case Racket, Board, Out
