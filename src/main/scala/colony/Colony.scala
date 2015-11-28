package colony

class Colony(foodAmountInit: Int) {
  private var _foodAmount = foodAmountInit

  def foodAmount = _foodAmount

  def foodAmount_=(newAmount: Int) {
    require(newAmount >= 0, "Negative food amount")
    _foodAmount = newAmount
  }
  
  private var _scoreAmount = 0

  def scoreAmount = _scoreAmount

  def incrAmount(incr: Int) {
    require(incr >= 0, "Negative score amount")
    _scoreAmount = _scoreAmount + incr
  }
  
}
