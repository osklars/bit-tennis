package model

import upickle.default.*

enum Player derives ReadWriter:
  case A, B
  
  lazy val opponent: Player = this match
    case Player.A => Player.B
    case Player.B => Player.A