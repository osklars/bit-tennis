package model.api.in

import model.types.{InputAction, Player, Side}
import upickle.default.*
  
case class Input
(
  action: InputAction,
  side: Side,
) derives ReadWriter
