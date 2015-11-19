package colony

class Colony(initValue: Int) {
  private var foodAmount = initValue

  def getFoodAmount = foodAmount

  def setFoodAmount(newAmount: Int) {
    assert(newAmount >= 0)
    foodAmount = newAmount
  }
}
