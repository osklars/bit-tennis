package model.pingis

import model.api.in.{Event, Input}
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
  def process(event: Event): Option[SetState] = game.process(event).map(handle)
  
  def process(input: Input): Option[SetState] = game.process(input).map(handle)
  
  private def handle(gameState: GameState): SetState = gameState match {
    case GameState(_, _, p, _) if p.Red >= 11 && p.Red >= p.Black + 2 =>
      copy(game = GameState(firstServer.opponent), points = points.inc(Player.Red))
    case GameState(_, _, p, _) if p.Black >= 11 && p.Black >= p.Red + 2 =>
      copy(game = GameState(firstServer.opponent), points = points.inc(Player.Black))
    case g => copy(game = g)
  }
  
