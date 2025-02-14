package model.pingis

import model.api.in.{Event, Input, NewMatch}
import model.types.InputAction.Reset
import model.types.{Player, Side}
import upickle.default.*

object MatchState:
  def apply(input: NewMatch): MatchState =
    MatchState(input.playerRed, input.playerBlack, input.bestOf, SetState(input.firstServer))

case class MatchState
(
  playerRed: String = "Player Red",
  playerBlack: String = "Player Black",
  bestOf: Int = 3,
  set: SetState = SetState(Player.Red),
):
  def process(event: Event): Option[MatchState] =
    set.process(event).map(newSet => copy(set = newSet))

  def process(input: Input): Option[MatchState] = input.action match
    case Reset =>
      Some(MatchState(set = SetState(if (input.side == Side.Left) Player.Red else Player.Black)))
    case _ => set.process(input).map(newSet => copy(set = newSet))
