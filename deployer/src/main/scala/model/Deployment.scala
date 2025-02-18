package model

import upickle.default.*

case class Deployment
(
  resourceId: String,
  repo: String,
  path: String,
  lastHash: String
) derives ReadWriter:
  def splitLastSegment: (String, String) =
    val lastSegmentIndex = path.lastIndexOf("/")
    if (lastSegmentIndex >= 0) (path.slice(0, lastSegmentIndex), path.slice(lastSegmentIndex + 1, path.length))
    else ("", path)
