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
  def process(event: Event): Option[GameState] =
    Option(rallyState, event).collect {
      // serving
      case (Idle, Event(Throw, Some(this.possession))) => copy(ToServe)

      case (ToServe, Event(Racket, Some(this.possession))) => copy(ToBounce1)
      case (ToServe, _) => copy(Idle) // ignore events until rally starts with a proper serve

      case (ToBounce1, Event(Board, Some(this.possession))) => copy(ToBounce2)
      case (ToBounce1, _) => handle(points.inc(possession.opponent))

      case (ToBounce2, Event(Board, Some(possession.opponent))) => copy(ToStrike, possession.opponent)
      case (ToBounce2, Event(Racket, Some(possession.opponent))) => handle(points.inc(possession))
      case (ToBounce2, Event(Net, _)) => copy(NetServe)
      case (ToBounce2, _) => handle(points.inc(possession.opponent))

      case (NetServe, Event(Board, Some(possession.opponent))) => copy(Idle)
      case (NetServe, Event(Net, _)) => this
      case (NetServe, _) => handle(points.inc(possession.opponent))

      // returning
      case (ToStrike, Event(Racket, Some(this.possession))) => copy(ToBounce, possession)
      case (ToStrike, _) => handle(points.inc(possession.opponent))

      case (ToBounce, Event(Board, Some(possession.opponent))) => copy(ToStrike, possession.opponent)
      case (ToBounce, Event(Racket, Some(possession.opponent))) => handle(points.inc(possession))
      case (ToBounce, Event(Net, _)) => this
      case (ToBounce, _) => handle(points.inc(possession.opponent))
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