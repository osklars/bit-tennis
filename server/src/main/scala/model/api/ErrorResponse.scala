package model.api

import upickle.default.*

case class ErrorResponse(error: String) derives ReadWriter
