package model

import model.RallyState.*
import upickle.default.*

case object SetState:
  def withFirstServer(firstServer: Player): SetState =
    SetState(
      GameState.withFirstServer(firstServer),
      Points(0, 0),
      firstServer,
    )

case class SetState
(
  game: GameState,
  points: Points,
  firstServer: Player
)derives ReadWriter:
  def process(event: GameEvent): SetState = game.process(event) match {
    case GameState(_, _, p, _) if p.A >= 11 && p.A >= p.B + 2 => awardPoint(Player.A)
    case GameState(_, _, p, _) if p.B >= 11 && p.B >= p.A + 2 => awardPoint(Player.B)
    case g => copy(game = g)
  }

  private def awardPoint(player: Player): SetState = {
    val newFirstServer = firstServer.opponent
    SetState(
      game = GameState(Idle, newFirstServer, Points(0, 0), firstServer),
      points = points.inc(player),
      firstServer = newFirstServer,
    )
  }
  
