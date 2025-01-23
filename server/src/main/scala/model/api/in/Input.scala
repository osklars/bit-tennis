package model.api.in

import model.types.{InputAction, Player}
import upickle.default.*
  
case class Input
(
  action: InputAction,
  player: Player,
) derives ReadWriter
