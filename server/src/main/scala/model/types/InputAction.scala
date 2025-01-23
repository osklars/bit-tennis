package model.types

import upickle.default.*

enum InputAction derives ReadWriter:
  case Increase, Decrease, Reset
