/**
  * Stocks actions to display them.
  */

package mvc

object LogsActions {
  var attacks: List[((Int, Int), (Int, Int))] = Nil

  def addAttack(attack: ((Int, Int), (Int, Int))): Unit = {
    attacks = attack::attacks
  }

  def emptyAttacks(): Unit = {
    attacks = Nil
  }
}
