package com.yang.akka

import akka.actor.{Actor, ActorRef}

class PlayerOne(val p2: ActorRef) extends Actor {
  override def receive: Receive = {
    case "start" => {
      println("开始操作")
      p2 ! "turn"
    }

    case "将军" => {
      println("p1 操作")
      Thread.sleep(1000)
      p2 ! "turn"
    }
  }
}
