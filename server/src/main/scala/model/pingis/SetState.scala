package model.pingis

import model.api.in.Event
import model.types.{Player, Points, RallyState}
import upickle.default.*

case object SetState:
  def apply(firstServer: Player): SetState =
    SetState(
      GameState(firstServer),
      firstServer = firstServer,
    )

case class SetState
(
  game: GameState,
  points: Points = Points(0, 0),
  firstServer: Player,
):
  def process(event: Event): Option[SetState] = game.process(event).map {
    case GameState(_, _, p, _) if p.A >= 11 && p.A >= p.B + 2 => awardPoint(Player.A)
    case GameState(_, _, p, _) if p.B >= 11 && p.B >= p.A + 2 => awardPoint(Player.B)
    case g => copy(game = g)
  }

  private def awardPoint(player: Player): SetState = {
    val newFirstServer = firstServer.opponent
    SetState(
      game = GameState(RallyState.Idle, newFirstServer, Points(0, 0), firstServer),
      points = points.inc(player),
      firstServer = newFirstServer,
    )
  }
  
