package model.pingis

import model.api.in.{DetectionEvent, NewMatch}
import model.types.Player
import upickle.default.*

object MatchState:
  def apply(input: NewMatch): MatchState =
    MatchState(input.playerA, input.playerB, input.bestOf, SetState(input.firstServer))

case class MatchState
(
  playerA: String = "Player A",
  playerB: String = "Player B",
  bestOf: Int = 3,
  set: SetState = SetState(Player.A),
):
  def process(event: DetectionEvent): MatchState = copy(set = set.process(event))
