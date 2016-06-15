package org.patricknoir.rec.app

import org.patricknoir.rec.interpreter.AccountServiceInterpreter
import org.patricknoir.rec.model.{ Selection, Account, Bet }
import org.patricknoir.rec.service.AccountService

object AccountServiceApp extends App with AccountService {

  val bet = Bet("BET1", Account("patrick"), (1 to 10).map(n => Selection(s"SEL$n")).toSet)

  val prg = for {
    _ <- placeBet(bet)
    selections <- selectionsPerAccount(Account("patrick"))
  } yield selections

  val result = prg.foldMap(AccountServiceInterpreter)

  println(result)

}
