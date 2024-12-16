package model

import upickle.default.*

enum BallEvent derives ReadWriter:
  case Throw, ExitTable, Net // only relevant for serves
  case Racket, Board, Out
