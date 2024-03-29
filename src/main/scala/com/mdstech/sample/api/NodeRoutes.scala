package com.mdstech.sample.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.sprayjson._
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.ask
import akka.util.Timeout
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.mdstech.sample.node.Node.{GetClusterMembers, GetFibonacci}
import com.mdstech.sample.node.processor.ProcessorResponse
import com.mdstech.sample.node.processor.ProcessorResponseJsonProtocol._


import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait NodeRoutes extends SprayJsonSupport {
  implicit def system: ActorSystem

  def node: ActorRef
  implicit lazy val timeout = Timeout(5.seconds)

  lazy val healthRoute : Route = pathPrefix("health") {
    concat(
      pathEnd {
        concat(
          get {
            complete(StatusCodes.OK)
          }
        )
      }
    )
  }

  lazy val statusRoutes : Route = pathPrefix("status") {
    concat(
      pathPrefix("members") {
        concat(
          pathEnd {
            concat(
              get {
                val membersFuture : Future[List[String]] = (node ? GetClusterMembers).mapTo[List[String]]
                onSuccess(membersFuture) { members =>
                  complete(StatusCodes.OK, members)
                }
              }
            )
          }
        )
      }
    )
  }

  lazy val processRoutes : Route = pathPrefix("process") {
    concat(
      pathPrefix("fibonacci") {
        concat(
          path(IntNumber) { n =>
            pathEnd {
              concat(
                get {
                  val processFuture: Future[ProcessorResponse] = (node ? GetFibonacci(n)).mapTo[ProcessorResponse]
                  onSuccess(processFuture) { response =>
                    complete(StatusCodes.OK, response)
                  }
                }
              )
            }
          }
        )
      }
    )
  }
}
