package model.pingis

import model.api.in.Event
import model.types.EventType.*
import model.types.RallyState.*
import model.types.{EventType, Player, Points, RallyState}

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
  def process(event: Event): Option[GameState] =
    Option(rallyState, event).collect {
      // serving
      case (Idle, Event(Throw, Some(this.possession))) => copy(ToServe)

      case (ToServe, Event(Racket, Some(this.possession))) => copy(ToBounce1)
      case (ToServe, _) => copy(Idle) // ignore events until rally starts with a proper serve

      case (ToBounce1, Event(Board, Some(this.possession))) => copy(ToBounce2)
      case (ToBounce1, _) => awardPoint(possession.opponent)

      case (ToBounce2, Event(Board, Some(possession.opponent))) => copy(ToStrike, possession.opponent)
      case (ToBounce2, Event(Racket, Some(possession.opponent))) => awardPoint(possession)
      case (ToBounce2, Event(Net, _)) => copy(NetServe)
      case (ToBounce2, _) => awardPoint(possession.opponent)

      case (NetServe, Event(Board, Some(possession.opponent))) => copy(Idle)
      case (NetServe, Event(Net, _)) => this
      case (NetServe, _) => awardPoint(possession.opponent)

      // returning
      case (ToStrike, Event(Racket, Some(this.possession))) => copy(ToBounce, possession)
      case (ToStrike, _) => awardPoint(possession.opponent)

      case (ToBounce, Event(Board, Some(possession.opponent))) => copy(ToStrike, possession.opponent)
      case (ToBounce, Event(Racket, Some(possession.opponent))) => awardPoint(possession)
      case (ToBounce, Event(Net, _)) => this
      case (ToBounce, _) => awardPoint(possession.opponent)
    }

  private def awardPoint(player: Player): GameState = {
    val newPoints = points.inc(player)
    copy(
      rallyState = Idle,
      possession =
        if (newPoints.A + newPoints.B < 20)
          if ((newPoints.A + newPoints.B) % 4 < 2) firstServer else firstServer.opponent
        else
          if ((newPoints.A + newPoints.B) % 2 == 0) firstServer else firstServer.opponent,
      points = newPoints,
    )
  }