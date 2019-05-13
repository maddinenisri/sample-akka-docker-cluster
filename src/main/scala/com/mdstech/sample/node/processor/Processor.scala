package com.mdstech.sample.node.processor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Processor {

  sealed trait ProcessorMessage

  case class ComputeFibonacci(n: Int) extends ProcessorMessage

  def props(nodeId: String) = Props(new Processor(nodeId))
}

class Processor(nodeId: String) extends Actor with ActorLogging {
  import Processor._

  val fibonacciProcessor: ActorRef = context.actorOf(FibonacciProcessor.props(nodeId), "fibonacci")
  override def receive: Receive = {
    case ComputeFibonacci(value) => {
      val replyTo = sender()
      import FibonacciProcessor._
      fibonacciProcessor ! Compute(value, replyTo)
    }
  }
}
