package org.patricknoir.rec.interpreter

import akka.actor.ActorSystem
import cats._
import org.patricknoir.rec.interpreter.actors.ReactiveAccountService
import org.patricknoir.rec.service._
import org.patricknoir.rec.model.{ Bet, Account }

import scala.concurrent.Future

object AccountServiceInterpreter extends (AccountServiceF ~> Id) {

  var accountBetsMap = Map.empty[Account, Set[Bet]]

  def apply[A](fa: AccountServiceF[A]) = fa match {
    case PlaceBet(b) =>
      println(s"Adding bet: $b for account: ${b.account}")
      accountBetsMap += b.account -> (accountBetsMap.getOrElse(b.account, Set.empty[Bet]) ++ Set(b))
      ()
    case BetsForAccount(account) =>
      println(s"Retrieving bets for account: $account")
      accountBetsMap(account)
    case SelectionsForBet(bet) =>
      println(s"Retrieving selections for bet: $bet")
      bet.selections
  }
}

object ReactiveASInterpreter extends (AccountServiceF ~> Future) {

  implicit val system = ActorSystem("freerec")
  import scala.concurrent.ExecutionContext.Implicits.global

  val accountService = new ReactiveAccountService()

  def apply[A](fa: AccountServiceF[A]) = fa match {
    case PlaceBet(b)         => accountService.placeBet(b)
    case BetsForAccount(a)   => accountService.betsForAccount(a)
    case SelectionsForBet(b) => accountService.selectionsForBet(b)
  }

}

