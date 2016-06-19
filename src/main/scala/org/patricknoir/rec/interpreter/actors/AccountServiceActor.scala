package org.patricknoir.rec.interpreter.actors

import akka.actor.{ ActorSystem, Props, Actor }
import akka.util.Timeout
import org.patricknoir.rec.model._
import org.patricknoir.rec.service.{ SelectionsForBet, BetsForAccount, PlaceBet }
import scala.concurrent.duration._
import akka.pattern.ask

import scala.concurrent.{ Future, ExecutionContext }

class AccountServiceActor extends Actor {

  var accountBetsMap = Map.empty[Account, Set[Bet]]

  def receive = {
    case PlaceBet(bet) =>
      println(s"Adding bet: $bet for account: ${bet.account}")
      accountBetsMap += bet.account -> (accountBetsMap.getOrElse(bet.account, Set.empty[Bet]) ++ Set(bet))
      sender ! true
    case BetsForAccount(account) =>
      println(s"Retrieving bets for account: $account")
      sender ! accountBetsMap(account)
    case SelectionsForBet(bet) =>
      println(s"Retrieving selections for bet: $bet")
      sender ! bet.selections
  }

}

object AccountServiceActor {
  lazy val props = Props(new AccountServiceActor)
}

class ReactiveAccountService(implicit system: ActorSystem, ec: ExecutionContext, timeout: Timeout = Timeout(30 seconds)) {

  val accountService = system.actorOf(AccountServiceActor.props, "accountService")

  def placeBet(bet: Bet): Future[Unit] = (accountService ? PlaceBet(bet)).mapTo[Boolean].map(_ => ())
  def betsForAccount(account: Account): Future[Set[Bet]] = (accountService ? BetsForAccount(account)).mapTo[Set[Bet]]
  def selectionsForBet(bet: Bet): Future[Set[Selection]] = (accountService ? SelectionsForBet(bet)).mapTo[Set[Selection]]

}
