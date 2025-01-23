package model.api.out

import model.api.in.{Event, Input, NewMatch}
import model.pingis.MatchState
import model.types.{Player, Points, RallyState}
import upickle.default.*

case class StateSummary
(
  latestEvent: Option[Event] = None,
  latestInput: Option[Input] = None,
  rallyState: RallyState,
  possession: Player,
  gamePoints: Points,
  setPoints: Points
) derives ReadWriter

object StateSummary:

  def apply(matchState: MatchState): StateSummary =
    StateSummary(
      rallyState = matchState.set.game.rallyState,
      possession = matchState.set.game.possession,
      gamePoints = matchState.set.game.points,
      setPoints = matchState.set.points,
    )

  def apply(event: Event, matchState: MatchState): StateSummary =
    StateSummary(matchState).copy(latestEvent = Some(event))

  def apply(input: Input, matchState: MatchState): StateSummary =
    StateSummary(matchState).copy(latestInput = Some(input))
