package model.types

import upickle.default.*

object Player: 
  def apply(side: Side, setPoints: Points): Player =
    if (((setPoints.Red + setPoints.Black) % 2 == 0) == (side == Side.Left)) Player.Red else Player.Black

enum Player derives ReadWriter:
  case Red, Black

  lazy val opponent: Player = this match
    case Player.Red => Player.Black
    case Player.Black => Player.Red