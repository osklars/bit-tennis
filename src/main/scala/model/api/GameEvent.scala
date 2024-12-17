package model.api

import model.Player
import upickle.default.*

sealed trait GameEvent

case class DetectionEvent
(
  detection: Detection,
  player: Option[Player],
  timestamp: Long
) extends GameEvent derives ReadWriter

case class InputEvent
(
  input: ManualInput,
  player: Option[Player],
  timestamp: Long
) extends GameEvent derives ReadWriter

object GameEvent:
  given rwDetectionEvent: ReadWriter[DetectionEvent] =
    readwriter[ujson.Value].bimap[DetectionEvent](
      d => writeJs(Map("detection" -> d.detection, "player" -> d.player, "timestamp" -> d.timestamp)),
      json => DetectionEvent(read[Detection](json("detection")), read[Option[Player]](json("player")), read[Long](json("timestamp")))
    )

  given rwInputEvent: ReadWriter[InputEvent] =
    readwriter[ujson.Value].bimap[InputEvent](
      d => writeJs(Map("input" -> d.input, "player" -> d.player, "timestamp" -> d.timestamp)),
      json => InputEvent(read[ManualInput](json("input")), read[Option[Player]](json("player")), read[Long](json("timestamp")))
    )

  given rwGameEvent: ReadWriter[GameEvent] =
    readwriter[ujson.Value].bimap[GameEvent](
      {
        case e: DetectionEvent => writeJs(e)
        case e: InputEvent => writeJs(e)
      },
      json => json("detection").strOpt match
        case Some(_) => read[DetectionEvent](json)
        case None => read[InputEvent](json)
    )