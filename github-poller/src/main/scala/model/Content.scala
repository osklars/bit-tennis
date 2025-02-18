package model

import upickle.default.*

case class Content
(
  name: String,
  path: String,
  sha: String
) derives ReadWriter
