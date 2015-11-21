package insects

import colony.Colony, places.Place

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Option[Place], cost: Int, _armor: Int = 1
                   , waterProof: Boolean = false)
  extends Insect(posX, posY, "ant_" + img, _place, _armor, waterProof) {

  private val _Cost = cost
  private val _Colony = colony

  if (_Colony.foodAmount < _Cost) throw new IllegalArgumentException("Not enough food.")
  //  require(!place.get.isAntIn)
  if (!place.get.isAntIn) {
    place.get.addAnt(this)
    _Colony.foodAmount_=(_Colony.foodAmount - _Cost)
  }

  def Cost: Int = _Cost
  def Colony: Colony = _Colony

  def addFood(amount: Int = 1) { _Colony.foodAmount_=(_Colony.foodAmount + amount) }
}

class HarvesterAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "harvester", colony, _place, 2) {

  override def moveActions() {
    addFood()
  }
}

class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place]
                 , armor: Int = 2, name: String = "thrower", waterProof: Boolean = false)
  extends Ant(posX, posY, name, colony, _place, armor) {

  override def moveActions() {
    if (place.get.isBeesIn){
      for (bee: Bee <- place.get.bees) {
        bee.armor_=(bee.armor - 1)
      }
    }
  }
}

class ShortThrower(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "shortthrower", colony, _place, 3) {

  override def moveActions() {
    var currentPlace: Option[Place] = place.get.entrance
    var i: Int = 2
    while (i > 0 && currentPlace.isDefined) {
      for (bee: Bee <- currentPlace.get.bees) {
        bee.armor_=(bee.armor - 1)
      }
      i -= 1
      currentPlace = currentPlace.get.entrance
    }
  }
}

class LongThrower(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "longthrower", colony, _place, 3) {

  override def moveActions() {
    scala.util.control.Exception.ignoring(classOf[NoSuchElementException]) {
      var currentPlace: Option[Place] = place.get.entrance.get.entrance.get.entrance
      while (currentPlace.isDefined) {
        for (bee: Bee <- currentPlace.get.bees) {
          bee.armor_=(bee.armor - 1)
        }
        currentPlace = currentPlace.get.entrance
      }
    }
  }
}

class FireAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "fire", colony, _place, 5) {

  override def armor_=(newArmor: Int): Unit = {
    armor_=(newArmor)
    if (armor <= 0) {
      kill()
      for (bee <- place.get.bees) bee.armor_=(bee.armor - 3)
    }
  }

  override def moveActions() {}
}

class ScubaAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends ThrowerAnt(posX, posY, colony, _place, armor = 5, name = "scuba", waterProof = true) {

}
