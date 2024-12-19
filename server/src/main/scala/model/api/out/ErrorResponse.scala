package model.api.out

import upickle.default.*

case class ErrorResponse(error: String) derives ReadWriter
