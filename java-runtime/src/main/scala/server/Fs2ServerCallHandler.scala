/*
 * Copyright (c) 2018 Gary Coady
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.lyranthe.fs2_grpc
package java_runtime
package server

import cats.effect._
import cats.effect.std.Dispatcher
import cats.implicits._
import fs2._
import io.grpc._

class Fs2ServerCallHandler[F[_]: Async] private (dispatcher: Dispatcher[F]) {

  def unaryToUnaryCall[Request, Response](
      implementation: (Request, Metadata) => F[Response],
      options: ServerCallOptions = ServerCallOptions.default
  ): ServerCallHandler[Request, Response] = new ServerCallHandler[Request, Response] {
    def startCall(call: ServerCall[Request, Response], headers: Metadata): ServerCall.Listener[Request] = {
      val listener = dispatcher.unsafeRunSync(Fs2UnaryServerCallListener[F](call, dispatcher, options))
      listener.unsafeUnaryResponse(headers, _ flatMap { request => implementation(request, headers) })
      listener
    }
  }

  def unaryToStreamingCall[Request, Response](
      implementation: (Request, Metadata) => Stream[F, Response],
      options: ServerCallOptions = ServerCallOptions.default
  ): ServerCallHandler[Request, Response] = new ServerCallHandler[Request, Response] {
    def startCall(call: ServerCall[Request, Response], headers: Metadata): ServerCall.Listener[Request] = {
      val listener = dispatcher.unsafeRunSync(Fs2UnaryServerCallListener[F](call, dispatcher, options))
      listener.unsafeStreamResponse(
        new Metadata(),
        v => Stream.eval(v) flatMap { request => implementation(request, headers) }
      )
      listener
    }
  }

  def streamingToUnaryCall[Request, Response](
      implementation: (Stream[F, Request], Metadata) => F[Response],
      options: ServerCallOptions = ServerCallOptions.default
  ): ServerCallHandler[Request, Response] = new ServerCallHandler[Request, Response] {
    def startCall(call: ServerCall[Request, Response], headers: Metadata): ServerCall.Listener[Request] = {
      val listener = dispatcher.unsafeRunSync(Fs2StreamServerCallListener[F](call, dispatcher, options))
      listener.unsafeUnaryResponse(headers, implementation(_, headers))
      listener
    }
  }

  def streamingToStreamingCall[Request, Response](
      implementation: (Stream[F, Request], Metadata) => Stream[F, Response],
      options: ServerCallOptions = ServerCallOptions.default
  ): ServerCallHandler[Request, Response] = new ServerCallHandler[Request, Response] {
    def startCall(call: ServerCall[Request, Response], headers: Metadata): ServerCall.Listener[Request] = {
      val listener = dispatcher.unsafeRunSync(Fs2StreamServerCallListener[F](call, dispatcher, options))
      listener.unsafeStreamResponse(headers, implementation(_, headers))
      listener
    }
  }
}

object Fs2ServerCallHandler {

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): Fs2ServerCallHandler[F] =
    new Fs2ServerCallHandler[F](dispatcher)
}
