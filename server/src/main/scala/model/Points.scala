package model

import upickle.default.*

case class Points(A: Int, B: Int) derives ReadWriter:
  def inc(player: Player): Points = player match {
    case Player.A => copy(A + 1)
    case Player.B => copy(B = B + 1)
  }
