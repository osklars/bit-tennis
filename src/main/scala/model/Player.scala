package model

import upickle.default.*

enum Player derives ReadWriter:
  case A, B

  val opponent: Player = this match
    case A => B
    case B => A
