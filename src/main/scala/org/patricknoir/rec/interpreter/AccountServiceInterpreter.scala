package org.patricknoir.rec.interpreter

import cats._
import org.patricknoir.rec.service._
import org.patricknoir.rec.model.{ Bet, Account }

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

