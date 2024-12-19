package model.pingis

import model.api.in.{DetectionEvent, NewMatch}
import upickle.default.*

object MatchState:
  def apply(input: NewMatch): MatchState =
    MatchState(input.playerA, input.playerB, input.bestOf, SetState(input.firstServer))

case class MatchState
(
  playerA: String,
  playerB: String,
  bestOf: Int,
  set: SetState,
):
  def process(event: DetectionEvent): MatchState = copy(set = set.process(event))
