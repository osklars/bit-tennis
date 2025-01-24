package model.pingis

import model.api.in.{Event, Input}
import model.types
import model.types.EventType.*
import model.types.InputAction.{Decrease, Increase}
import model.types.RallyState.*
import model.types.{EventType, InputAction, Player, Points, RallyState}

case object GameState:
  def apply(firstServer: Player): GameState =
    GameState(
      possession = firstServer,
      firstServer = firstServer,
    )

case class GameState
(
  rallyState: RallyState = Idle,
  possession: Player = Player.Red, // a player has possession of the ball until it's the other players turn to hit it
  points: Points = Points(0, 0),
  firstServer: Player = Player.Red,
):
  def process(event: EventType, player: Option[Player]): Option[GameState] =
    Option(rallyState, event, player).collect {
      // serving
      case (Idle, Throw, Some(this.possession)) => copy(ToServe)

      case (ToServe, Racket, Some(this.possession)) => copy(ToBounce1)
      case (ToServe, _, _) => copy(Idle) // ignore events until rally starts with a proper serve

      case (ToBounce1, Board, Some(this.possession)) => copy(ToBounce2)
      case (ToBounce1, _, _) => handle(points.inc(possession.opponent))

      case (ToBounce2, Board, Some(possession.opponent)) => copy(ToStrike, possession.opponent)
      case (ToBounce2, Racket, Some(possession.opponent)) => handle(points.inc(possession))
      case (ToBounce2, Net, _) => copy(NetServe)
      case (ToBounce2, _, _) => handle(points.inc(possession.opponent))

      case (NetServe, Board, Some(possession.opponent)) => copy(Idle)
      case (NetServe, Net, _) => this
      case (NetServe, _, _) => handle(points.inc(possession.opponent))

      // returning
      case (ToStrike, Racket, Some(this.possession)) => copy(ToBounce, possession)
      case (ToStrike, _, _) => handle(points.inc(possession.opponent))

      case (ToBounce, Board, Some(possession.opponent)) => copy(ToStrike, possession.opponent)
      case (ToBounce, Racket, Some(possession.opponent)) => handle(points.inc(possession))
      case (ToBounce, Net, _) => this
      case (ToBounce, _, _) => handle(points.inc(possession.opponent))
    }
  
  private def handle(points: Points): GameState =
    copy(
      rallyState = Idle,
      possession =
        if (points.Red + points.Black < 20)
          if ((points.Red + points.Black) % 4 < 2) firstServer else firstServer.opponent
        else
          if ((points.Red + points.Black) % 2 == 0) firstServer else firstServer.opponent,
      points = points,
    )
  
  def process(action: InputAction, player: Player): Option[GameState] = Option(action).collect {
    case Increase => handle(points.inc(player))
    case Decrease => handle(points.dec(player))
  }