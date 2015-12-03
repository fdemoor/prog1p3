package colony

/**
  * Stores the food and score of the player.
  *
  * @param foodAmountInit Initial amount of food at the start of the game.
  */
class Colony(foodAmountInit: Int) {
  private var _foodAmount = foodAmountInit

  def foodAmount = _foodAmount

  def foodAmount_=(newAmount: Int) {
    require(newAmount >= 0, "Negative food amount")
    _foodAmount = newAmount
  }

  private var _scoreAmount = 0

  def scoreAmount = _scoreAmount

  def incrScoreAmount(incr: Int) {
    require(incr >= 0, "Negative score incr")
    _scoreAmount = _scoreAmount + incr
  }
}
