package insects

import colony.Colony, places.Place

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Option[Place]
                   , cost: Int = 1, _armor: Int = 1
                   , waterProof: Boolean = false, _blocksPath: Boolean = true, container: Boolean = false)
  extends Insect(posX, posY, "ant_" + img, _place, _armor, waterProof) {

  private val _Cost = cost
  private val _Colony = colony

  if (_Colony.foodAmount < _Cost) throw new IllegalArgumentException("Not enough food.")
  //  require(!place.get.isAntIn)
  if (!container && !place.get.isAntIn) {
    place.get.addAnt(this)
    _Colony.foodAmount_=(_Colony.foodAmount - _Cost)
  }

  def Cost: Int = _Cost
  def Colony: Colony = _Colony
  def blocksPath: Boolean = _blocksPath
  def isContainer: Boolean = container
  def isProtected: Boolean = place.get.bodyguard.isDefined
  def bodyguard: Option[BodyguardAnt] = place.get.bodyguard
  /** Get the all the places of the tunnel. */
  def places: List[Place] = {
    var currentPlace: Place = place.get
    var res: List[Place] = List(currentPlace)
    while (currentPlace.entrance.isDefined) {
      currentPlace = currentPlace.entrance.get
      res = currentPlace::res
    }
    currentPlace = place.get
    while (currentPlace.exit.isDefined) {
      currentPlace = currentPlace.exit.get
      res = currentPlace::res
    }
    res
  }

  def addFood(amount: Int = 1) { _Colony.foodAmount_=(_Colony.foodAmount + amount) }

  override def armor_=(newArmor: Int) {
    if (newArmor < armor) {  // Damages are done the the ant
      if (isContainer || !isProtected) {
        super.armor_=(newArmor)
      } else {
        // Reroute the damages to the bodyguard and take what's left
        val damages = armor - newArmor
        val remainingDamages = damages - bodyguard.get.armor
        if (remainingDamages > 0) {
          bodyguard.get.armor_=(0)
          super.armor_=(armor - remainingDamages)
        }
        else bodyguard.get.armor_=(bodyguard.get.armor - damages)
      }
    }
  }
}

class HarvesterAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "harvester", colony, _place, cost = 2) {

  override def moveActions() {
    addFood()
  }
}

class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place], cost: Int = 2
                 , armor: Int = 1, name: String = "thrower", waterProof: Boolean = false, blocksPath: Boolean = true)
  extends Ant(posX, posY, name, colony, _place, cost, armor, _blocksPath = blocksPath) {

  override def moveActions() {
    if (place.get.isBeesIn){
      for (bee: Bee <- place.get.bees) {
        bee.armor_=(bee.armor - 1)
      }
    }
  }
}

class ScubaAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends ThrowerAnt(posX, posY, colony, _place, cost = 5, name = "scuba", waterProof = true)

class NinjaAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends ThrowerAnt(posX, posY, colony, _place, cost = 6, name = "ninja", blocksPath = false)

class ShortThrower(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "shortthrower", colony, _place, cost = 3) {

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

/** Ant that does damages when it dies. */
class FireAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "fire", colony, _place, cost = 5) {

  override def armor_=(newArmor: Int): Unit = {
    armor_=(newArmor)
    if (armor <= 0) {
      kill()
      for (bee <- place.get.bees) bee.armor_=(bee.armor - 3)
    }
  }

  override def moveActions() {}
}

/** Ant that does nothing but block enemies. */
class WallAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "wall", colony, _place, 4, 4) {

  override def moveActions() {}
}

/** Ant that kill a random bee in its tunnel each 3 turns. */
class HungryAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "hungry", colony, _place, cost = 4) {

  private var turnAwait: Int = 0

  /** Get a random bee and kill it. */
  override def moveActions(): Unit = {
    if (turnAwait > 0) turnAwait -= 1
    else {
      var placesNotChecked: List[Place] = util.Random.shuffle(places)
      var hasKilledBee: Boolean = false
      while (!hasKilledBee && placesNotChecked != Nil) {
        val currentPlace: Place = placesNotChecked.head
        if (currentPlace.isBeesIn) {
          currentPlace.bees.head.armor_=(0)
          hasKilledBee = true
        }
        placesNotChecked = placesNotChecked.tail
      }
      if (hasKilledBee) turnAwait = 3
    }
  }
}

class BodyguardAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place], antInit: Option[Ant] = None)
  extends Ant(posX, posY, "weeds", colony, _place, cost = 4, container = true, _armor = 2, waterProof = true) {

  place.get.addAnt(this)
  Colony.foodAmount_=(Colony.foodAmount - Cost)

  private var _ant: Option[Ant] = antInit

  def ant: Option[Ant] = _ant
  def canAddAnt: Boolean = ant.isEmpty

  def ant_=(modifiedAnt: Option[Ant]) {
    if (modifiedAnt.isDefined && ant.isDefined) throw new  IllegalArgumentException("It already contains an ant.")
    _ant = modifiedAnt
  }

  override def moveActions() {}
}
