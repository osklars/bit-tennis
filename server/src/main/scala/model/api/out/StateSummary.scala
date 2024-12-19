package model.api.out

import model.InternalState
import model.api.in.{DetectionEvent, NewMatch}
import model.types.{Player, Points, RallyState}
import upickle.default.*

case class StateSummary
(
  event: DetectionEvent,
  rallyState: RallyState,
  possession: Player,
  gamePoints: Points,
  setPoints: Points
)derives ReadWriter

object StateSummary:
  def apply(state: InternalState): StateSummary =
    StateSummary(
      event = state.event,
      rallyState = state.matchState.set.game.rallyState,
      possession = state.matchState.set.game.possession,
      gamePoints = state.matchState.set.game.points,
      setPoints = state.matchState.set.points,
    )
