package colony

class Colony(foodAmountInit: Int) {
  private var _foodAmount = foodAmountInit

  def foodAmount = _foodAmount

  def foodAmount_=(newAmount: Int) {
    require(newAmount >= 0, "Negative food amount")
    _foodAmount = newAmount
  }
}
