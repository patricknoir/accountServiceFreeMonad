package org.patricknoir.rec.app

import org.patricknoir.rec.interpreter.{ ReactiveASInterpreter, AccountServiceInterpreter }
import org.patricknoir.rec.model._
import org.patricknoir.rec.service.AccountService
import cats.std.all._
import cats.data._
import cats._
import cats.syntax.all._
import scala.concurrent.ExecutionContext.Implicits._

object AccountServiceApp extends App with AccountService {

  val bet = Bet("BET-1", Account("patrick"), (1 to 10).map(n => Selection(s"SEL-$n")).toSet)

  val prg = for {
    _ <- placeBet(bet)
    selections <- selectionsPerAccount(Account("patrick"))
  } yield selections

  val result = prg.foldMap(ReactiveASInterpreter) //foldMap(AccountServiceInterpreter)

  println(result)

}
