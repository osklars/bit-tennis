package model.api.out

import model.InternalState
import model.api.in.{Event, NewMatch}
import model.pingis.MatchState
import model.types.{Player, Points, RallyState}
import upickle.default.*

case class StateSummary
(
  latestEvent: Option[Event],
  rallyState: RallyState,
  possession: Player,
  gamePoints: Points,
  setPoints: Points
) derives ReadWriter

object StateSummary:

  def apply(event: Event, matchState: MatchState): StateSummary =
    StateSummary(
      latestEvent = Some(event),
      rallyState = matchState.set.game.rallyState,
      possession = matchState.set.game.possession,
      gamePoints = matchState.set.game.points,
      setPoints = matchState.set.points,
    )
  
  def apply(matchState: MatchState): StateSummary =
    StateSummary(
      latestEvent = None,
      rallyState = matchState.set.game.rallyState,
      possession = matchState.set.game.possession,
      gamePoints = matchState.set.game.points,
      setPoints = matchState.set.points,
    )
