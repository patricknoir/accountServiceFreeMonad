package org.patricknoir.rec.service

import cats.free.Free
import org.patricknoir.rec.model._

/**
 * Created by patrick on 13/06/2016.
 */
trait AccountService {

  type Response[A] = Free[AccountServiceF, A]

  def placeBet(bet: Bet): Response[Unit] = Free.liftF(PlaceBet(bet))
  def betsForAccount(account: Account): Response[Set[Bet]] = Free.liftF(BetsForAccount(account))
  def selectionsForBet(bet: Bet): Response[Set[Selection]] = Free.liftF(SelectionsForBet(bet))

  def selectionsPerAccount(account: Account): Response[Set[Selection]] = for {
    bets <- betsForAccount(account)
    sels <- bets.map(selectionsForBet).reduce { (resp1, resp2) =>
      resp1.flatMap(s1 => resp2.map(s2 => s1 ++ s2))
    }
  } yield sels

}
