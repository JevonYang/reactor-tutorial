package com.yang.akka

import akka.actor.{ActorSystem, Props}

object ChineseChess {

  private val chineseChessActorSystem = ActorSystem("ChineseChess")

  private val p2 = chineseChessActorSystem.actorOf(Props[PlayerTwo], "player2")

  private val p1 = chineseChessActorSystem.actorOf(Props(new PlayerOne(p2)), "player1")

  def main(args: Array[String]): Unit = {
    p2 ! "start"
    p1 ! "start"
  }
}
