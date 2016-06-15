package org.patricknoir.rec.service

import org.patricknoir.rec.model.{ Account, Bet, Selection }

sealed trait AccountServiceF[+A]
case class PlaceBet(bet: Bet) extends AccountServiceF[Unit]
case class BetsForAccount(account: Account) extends AccountServiceF[Set[Bet]]
case class SelectionsForBet(bet: Bet) extends AccountServiceF[Set[Selection]]

