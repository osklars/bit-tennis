package model.api.in

import model.types.Player
import upickle.default.*

case class NewMatch
(
  playerRed: String,
  playerBlack: String,
  bestOf: Int,
  firstServer: Player,
) derives ReadWriter
