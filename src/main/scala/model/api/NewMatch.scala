package model.api

import model.Player
import upickle.default.*

case class NewMatch
(
  playerA: String,
  playerB: String,
  bestOf: Int,
  firstServer: Player
) derives ReadWriter
