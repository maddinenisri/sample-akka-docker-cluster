package com.mdstech.sample.node.processor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.annotation.tailrec

object FibonacciProcessor {

  sealed trait ProcessorFibonacciMessage
  case class Compute(n: Int, replyTo: ActorRef) extends ProcessorFibonacciMessage

  def props(nodeId: String) = Props(new FibonacciProcessor(nodeId))

  def fibonacci(x: Int): BigInt = {
    @tailrec def fibHelper(x: Int, prev: BigInt = 0, next: BigInt = 1): BigInt = x match {
      case 0 => prev
      case 1 => next
      case _ => fibHelper(x - 1, next, next + prev)
    }
    fibHelper(x)
  }

}

class FibonacciProcessor(nodeId: String) extends Actor with ActorLogging {

  import FibonacciProcessor._

  override def receive: Receive = {
    case Compute(value, replyTo) => {
      replyTo ! ProcessorResponse(nodeId, fibonacci(value))
    }
  }

}
