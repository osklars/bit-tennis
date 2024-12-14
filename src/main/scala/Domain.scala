package com.example

import cats.effect.{Concurrent}
import org.http4s.{EntityDecoder, EntityEncoder}
import upickle.default.*

enum BallEvent derives ReadWriter:
  case Racket, Board, Net, Out

enum Player derives ReadWriter:
  case A, B

enum RallyState derives ReadWriter:
  case ToServe, ServeRacket, ServeOwnBoard, ServeNet, ServeBoard
  case ToStrike, Racket, Net, Board

case class GameEvent
(
  eventType: BallEvent,
  player: Player,
  timestamp: Long
) derives ReadWriter

case class GameState
(
  rallyState: RallyState,
  server: Player,
  toStrike: Player,
  points: Map[Player, Int]
) derives ReadWriter:
  def process(event: GameEvent): GameState =
    (rallyState, event) match
      case (RallyState.ToServe, GameEvent(BallEvent.Racket, p, _)) if p == server =>
        copy(rallyState = RallyState.ServeRacket)
      case (RallyState.ServeRacket, GameEvent(BallEvent.Board, p, _)) if p == server =>
        copy(rallyState = RallyState.ServeOwnBoard)
      case (RallyState.ServeOwnBoard, GameEvent(BallEvent.Board, _, _)) =>
        copy(rallyState = RallyState.ToStrike, toStrike = if server == Player.A then Player.B else Player.A)
      case (RallyState.ToStrike, GameEvent(BallEvent.Racket, p, _)) if p == toStrike =>
        copy(rallyState = RallyState.Racket, toStrike = if p == Player.A then Player.B else Player.A)
      // Add more cases...
      case _ => this

  def isServing: Boolean = rallyState.toString.startsWith("Serve")

object GameState:
  def initial(server: Player) = GameState(
    rallyState = RallyState.ToServe,
    server = server,
    toStrike = server,
    points = Map(Player.A -> 0, Player.B -> 0)
  )

object Codecs:
  import org.http4s.*

  implicit def upickleEncoder[F[_], A: Writer]: EntityEncoder[F, A] =
    EntityEncoder.stringEncoder[F].contramap[A](write[A](_))

  implicit def upickleDecoder[F[_]: Concurrent, A: Reader]: EntityDecoder[F, A] =
    EntityDecoder.text[F].map(read[A](_))