package model

import model.api.{DetectionEvent, NewMatch}
import upickle.default.*

object MatchState:
  def newMatch(input: NewMatch): MatchState =
    MatchState(input.playerA, input.playerB, input.bestOf, SetState.withFirstServer(input.firstServer))

case class MatchState
(
  playerA: String,
  playerB: String,
  bestOf: Int,
  set: SetState,
) derives ReadWriter:
  def process(event: DetectionEvent): MatchState = copy(set = set.process(event))
