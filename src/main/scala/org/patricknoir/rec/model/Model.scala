package org.patricknoir.rec.model

case class Account(accountNo: String)
case class Selection(id: String)
case class Bet(id: String, account: Account, selections: Set[Selection])