package colony

class Colony(foodAmountInit: Int) {
  private var _foodAmount = foodAmountInit

  def foodAmount = _foodAmount

  def foodAmount_=(newAmount: Int) {
    assert(newAmount >= 0)
    _foodAmount = newAmount
  }
}
