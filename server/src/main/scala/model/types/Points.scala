package model.types

import upickle.default.*

case class Points(Red: Int, Black: Int) derives ReadWriter:
  def inc(player: Player): Points = player match {
    case Player.Red => copy(Red = Red + 1)
    case Player.Black => copy(Black = Black + 1)
  }
  
  def dec(player: Player): Points = player match {
    case Player.Red => copy(Red = Math.max(Red - 1, 0))
    case Player.Black => copy(Black = Math.max(Black - 1, 0))
  }
