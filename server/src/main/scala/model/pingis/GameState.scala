package model.pingis

import model.types.RallyState.*
import model.api.in.DetectionEvent
import model.types.Detection.*
import model.types.{Detection, Player, Points, RallyState}

case object GameState:
  def apply(firstServer: Player): GameState =
    GameState(
      possession = firstServer,
      firstServer = firstServer,
    )

case class GameState
(
  rallyState: RallyState = Idle,
  possession: Player = Player.A, // a player has possession of the ball until it's the other players turn to hit it
  points: Points = Points(0, 0),
  firstServer: Player = Player.A,
):
  def process(event: DetectionEvent): GameState =
    (rallyState, event) match
      // serving
      case (_, DetectionEvent(Throw, Some(this.possession), _)) => copy(ToServe)

      case (ToServe, DetectionEvent(Racket, Some(this.possession), _)) => copy(ToBounce1)
      case (ToServe, _) => copy(Idle) // ignore events until rally starts with a proper serve

      case (ToBounce1, DetectionEvent(Board, Some(this.possession), _)) => copy(ToBounce2)
      case (ToBounce1, _) => awardPoint(possession.opponent)

      case (ToBounce2, DetectionEvent(Board, Some(possession.opponent), _)) => copy(ToStrike, possession.opponent)
      case (ToBounce2, DetectionEvent(Racket, Some(possession.opponent), _)) => awardPoint(possession)
      case (ToBounce2, DetectionEvent(Net, _, _)) => copy(NetServe)
      case (ToBounce2, _) => awardPoint(possession.opponent)

      case (NetServe, DetectionEvent(Board, Some(possession.opponent), _)) => copy(Idle)
      case (NetServe, DetectionEvent(Net, _, _)) => this
      case (NetServe, _) => awardPoint(possession.opponent)

      // returning
      case (ToStrike, DetectionEvent(Detection.Racket, Some(this.possession), _)) => copy(ToBounce, possession)
      case (ToStrike, _) => awardPoint(possession.opponent)

      case (ToBounce, DetectionEvent(Detection.Board, Some(possession.opponent), _)) => copy(ToStrike, possession.opponent)
      case (ToBounce, DetectionEvent(Racket, Some(possession.opponent), _)) => awardPoint(possession)
      case (ToBounce, DetectionEvent(Net, _, _)) => this
      case (ToBounce, _) => awardPoint(possession.opponent)

      case _ => copy(Error(s"Unhandled case $this $event"))

  private def awardPoint(player: Player): GameState =
    GameState(
      rallyState = Idle,
      possession = if (points.A + points.B % 4 < 2) firstServer else firstServer.opponent,
      points = points.inc(player),
      firstServer,
    )