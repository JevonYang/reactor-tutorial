package com.yang.akka

import akka.actor.{Actor, ActorSystem, Props}

class HelloActor extends Actor{

  override def receive: Receive = {
    case "1" => println("ojbk")
    case "2" => println("received 2")
    case "stop" => {
      println("stopped!")
      context.stop(self)
      context.system.terminate()
    }
  }
}
