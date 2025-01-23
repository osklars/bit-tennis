package model.api.out

import model.api.in.{Event, Input}
import model.pingis.MatchState
import model.types.{Player, Points, RallyState}
import upickle.default.*

case class StateSummary
(
  // Only with vision
  latestEvent: Option[Event] = None,
  rallyState: RallyState,
  possession: Player,
  // Needed for UI
  latestInput: Option[Input] = None,
  firstServer: Player,
  gamePoints: Points,
  setPoints: Points,
  playerRed: String,
  playerBlack: String,
  bestOf: Int,
) derives ReadWriter

object StateSummary:

  def apply(matchState: MatchState): StateSummary =
    StateSummary(
      rallyState = matchState.set.game.rallyState,
      possession = matchState.set.game.possession,
      firstServer = matchState.set.game.firstServer,
      gamePoints = matchState.set.game.points,
      setPoints = matchState.set.points,
      playerRed = matchState.playerRed,
      playerBlack = matchState.playerBlack,
      bestOf = matchState.bestOf,
    )

  def apply(event: Event, matchState: MatchState): StateSummary =
    StateSummary(matchState).copy(latestEvent = Some(event))

  def apply(input: Input, matchState: MatchState): StateSummary =
    StateSummary(matchState).copy(latestInput = Some(input))
