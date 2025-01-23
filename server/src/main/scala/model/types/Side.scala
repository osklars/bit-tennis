package model.types

import upickle.default.*

enum Side derives ReadWriter:
  case Left, Right