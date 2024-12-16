package model

import model.BallEvent.*
import model.RallyState.*
import upickle.default.*


object GameState:
  def withFirstServer(firstServer: Player) =
    GameState(
      rallyState = Idle,
      possession = firstServer,
      points = Points(0, 0),
      firstServer = firstServer,
    )

case class GameState
(
  rallyState: RallyState,
  possession: Player, // a player has possession of the ball until it's the other players turn to hit it
  points: Points,
  firstServer: Player,
) derives ReadWriter:
  def process(event: GameEvent): GameState =
    (rallyState, event) match
      // serving
      case (_, GameEvent(Throw, Some(this.possession), _)) => copy(ToServe)

      case (ToServe, GameEvent(Racket, Some(this.possession), _)) => copy(ToBounce1)
      case (ToServe, _) => copy(Idle) // ignore events until rally starts with a proper serve

      case (ToBounce1, GameEvent(Board, Some(this.possession), _)) => copy(ToBounce2)
      case (ToBounce1, _) => awardPoint(possession.opponent)

      case (ToBounce2, GameEvent(Board, Some(possession.opponent), _)) => copy(ToStrike, possession.opponent)
      case (ToBounce2, GameEvent(Racket, Some(possession.opponent), _)) => awardPoint(possession)
      case (ToBounce2, GameEvent(Net, _, _)) => copy(NetServe)
      case (ToBounce2, _) => awardPoint(possession.opponent)

      case (NetServe, GameEvent(Board, Some(possession.opponent), _)) => copy(Idle)
      case (NetServe, GameEvent(Net, _, _)) => this
      case (NetServe, _) => awardPoint(possession.opponent)

      // returning
      case (ToStrike, GameEvent(BallEvent.Racket, Some(this.possession), _)) => copy(ToBounce, possession)
      case (ToStrike, _) => awardPoint(possession.opponent)

      case (ToBounce, GameEvent(BallEvent.Board, Some(possession.opponent), _)) => copy(ToStrike, possession.opponent)
      case (ToBounce, GameEvent(Racket, Some(possession.opponent), _)) => awardPoint(possession)
      case (ToBounce, GameEvent(Net, _, _)) => this
      case (ToBounce, _) => awardPoint(possession.opponent)

      case _ => copy(Error(s"Unhandled case $this $event"))

  private def awardPoint(player: Player): GameState =
    GameState(
      rallyState = Idle,
      possession = if (points.A + points.B % 4 < 2) firstServer else firstServer.opponent,
      points = points.inc(player),
      firstServer,
    )