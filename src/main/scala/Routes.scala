package com.example

import cats.effect.Concurrent
import cats.syntax.all.*
import org.http4s.{EntityEncoder, Header, HttpRoutes, MediaType}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.middleware.CORS
import org.typelevel.ci.CIStringSyntax
import upickle.default.*

class Routes[F[_] : Concurrent](manager: StateManager[F]) extends Http4sDsl[F]:
  import Codecs.*
  val routes: HttpRoutes[F] = CORS.policy.withAllowOriginAll(HttpRoutes.of[F] {
    case req@POST -> Root / "event" =>
      for
        event <- req.as[GameEvent]
        state <- manager.process(event)
        resp <- Ok(state)
      yield resp

    case GET -> Root / "state" =>
      val stream = manager.subscribe
        .map(state => write(state))
        .through(fs2.text.utf8.encode)

      Ok(stream).map(_.withHeaders(
        `Content-Type`(MediaType.`text/event-stream`),
        Header.Raw(ci"Cache-Control", "no-cache"),
        Header.Raw(ci"Connection", "keep-alive")
      ))
  })