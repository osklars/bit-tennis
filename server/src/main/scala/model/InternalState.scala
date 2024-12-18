package model

import model.api.in.{DetectionEvent, NewMatch}
import model.pingis.MatchState

case class InternalState
(
  event: Either[DetectionEvent, NewMatch],
  matchState: MatchState,
  timestamp: Long,
):
  def process(event: Either[DetectionEvent, NewMatch], timestamp: Long): InternalState =
    copy(
      event = event,
      matchState = event match
        case Left(detection) => matchState.process(detection)
        case Right(newMatch) => MatchState(newMatch),
      timestamp = timestamp
    )



