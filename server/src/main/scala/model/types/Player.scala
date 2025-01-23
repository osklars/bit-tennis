package model.types

import upickle.default.*

enum Player derives ReadWriter:
  case Red, Black

  lazy val opponent: Player = this match
    case Player.Red => Player.Black
    case Player.Black => Player.Red