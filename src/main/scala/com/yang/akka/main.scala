package com.yang.akka

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object main {

  private val factory = ActorSystem("factory")

  private val helloActorRef = factory.actorOf(Props[HelloActor], "helloActor")

  def main(args: Array[String]): Unit = {
    var flag = true

    while (flag) {
      print("请输入信息：")
      val consoleLine: String = StdIn.readLine()

      helloActorRef ! consoleLine
      if (consoleLine.equals("stop")) {
        flag = false
        println("===============")
      }

      Thread.sleep(100)
    }
  }
}
