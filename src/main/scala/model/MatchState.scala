package model

import model.api.NewMatch
import upickle.default.*

object MatchState:
  def newMatch(input: NewMatch): MatchState =
    MatchState(input.playerA, input.playerB, input.bestOf, SetState.withFirstServer(input.firstServer), None)

case class MatchState
(
  playerA: String,
  playerB: String,
  bestOf: Int,
  set: SetState,
  winner: Option[Player],
) derives ReadWriter:
  def process(event: GameEvent): MatchState = set.process(event) match {
    case SetState(_, p, _) if p.A + p.B >= bestOf =>
      copy(winner = Option(if (p.A > p.B) Player.A else Player.B))
    case set => copy(set = set)
  }
