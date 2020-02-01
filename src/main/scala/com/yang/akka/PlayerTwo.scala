package com.yang.akka

import akka.actor.Actor

class PlayerTwo extends Actor{
  override def receive: Receive = {
    case "start" => println("ok")
    case "turn" => {
      println("p2 操作")
      Thread.sleep(1000)
      sender() ! "将军"
    }
  }
}
