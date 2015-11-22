package insects

import colony.Colony, places.Place

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Option[Place]
                   , cost: Int = 1, _armor: Int = 1, damagesAmount: Int = 0
                   , waterProof: Boolean = false, _blocksPath: Boolean = true, container: Boolean = false)
  extends Insect(posX, posY, "ant_" + img, _place, _armor, waterProof, damagesAmount = damagesAmount) {

  private val _Cost = cost
  private val _Colony = colony

  if (_Colony.foodAmount < _Cost) throw new IllegalArgumentException("Not enough food.")
  if (place.get.canAddAnt(this)) {
    place.get.addAnt(this)
    _Colony.foodAmount_=(_Colony.foodAmount - _Cost)
  }

  def Cost: Int = _Cost
  def Colony: Colony = _Colony
  def blocksPath: Boolean = _blocksPath
  def isContainer: Boolean = container
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
}

class HarvesterAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "harvester", colony, _place, cost = 2) {

  override def moveActions() {
    addFood()
  }
}

class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place], cost: Int = 2
                 , armor: Int = 1, name: String = "thrower", waterProof: Boolean = false, blocksPath: Boolean = true)
  extends Ant(posX, posY, name, colony, _place, cost, armor, _blocksPath = blocksPath, damagesAmount = 1) {

  override def moveActions() {
    var hasHitBee: Boolean = false
    var bL: List[Bee] = place.get.bees
    while (bL.nonEmpty && !hasHitBee) {
      val bee: Bee = bL.head
      if (!bee.isDead) {
        bee.armor_=(bee.armor - damages)
        hasHitBee = true
      }
      bL = bL.tail
    }
  }
}

class ScubaAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place]
               , cost: Int = 5, name: String = "scuba", armor: Int = 1)
  extends ThrowerAnt(posX, posY, colony, _place, cost = cost, name = name, armor = armor, waterProof = true)

class QueenAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends ScubaAnt(posX, posY, colony, _place, cost = 6, name = "queen", armor = 2) {

  /* Kill it if there's already a queen. */
  for (p <- places if p != place.get) {
    if (p.isAntIn && (p.ant.isInstanceOf[QueenAnt] ||
      (p.ant.isContainer &&
        p.ant.asInstanceOf[BodyguardAnt].ant.isDefined &&
        p.ant.asInstanceOf[BodyguardAnt].ant.get.isInstanceOf[QueenAnt]))) armor_=(0)
  }

  override def moveActions(): Unit = {
    super.moveActions()
    for (p <- places) {
      if (p.isAntIn) {
        if (!p.ant.isContainer) p.ant.doubleDamages()
        else if (p.ant.asInstanceOf[BodyguardAnt].ant.isDefined) p.ant.asInstanceOf[BodyguardAnt].ant.get.doubleDamages()
      }
    }
  }
}

class NinjaAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends ThrowerAnt(posX, posY, colony, _place, cost = 6, name = "ninja", blocksPath = false)

class ShortThrower(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "shortthrower", colony, _place, cost = 3, damagesAmount = 1) {

  override def moveActions() {
    var currentPlace: Option[Place] = place.get.entrance
    var i: Int = 2
    var hasHitBee: Boolean = false
    while (i > 0 && currentPlace.isDefined && !hasHitBee) {
      var bL: List[Bee] = currentPlace.get.bees
      while (bL.nonEmpty && !hasHitBee) {
        val bee: Bee = currentPlace.get.bees.head
        if (!bee.isDead) {
          bee.armor_=(bee.armor - damages)
          hasHitBee = true
        }
        bL = bL.tail
      }
      i -= 1
      currentPlace = currentPlace.get.entrance
    }
  }
}

class LongThrower(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "longthrower", colony, _place, 3, damagesAmount = 1) {

  override def moveActions() {
    scala.util.control.Exception.ignoring(classOf[NoSuchElementException]) {
      var currentPlace: Option[Place] = place.get.entrance.get.entrance.get.entrance
      var hasHitBee: Boolean = false
      while (currentPlace.isDefined && !hasHitBee) {
        var bL: List[Bee] = currentPlace.get.bees
        while (bL.nonEmpty && !hasHitBee) {
          val bee: Bee = currentPlace.get.bees.head
          if (!bee.isDead) {
            bee.armor_=(bee.armor - damages)
            hasHitBee = true
          }
          bL = bL.tail
        }
        currentPlace = currentPlace.get.entrance
      }
    }
  }
}

/** Ant that does damages when it dies. */
class FireAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "fire", colony, _place, cost = 5, damagesAmount = 3) {

  override def armor_=(newArmor: Int): Unit = {
    armor_=(newArmor)
    if (armor <= 0) {
      kill()
      for (bee <- place.get.bees) bee.armor_=(bee.armor - damages)
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
          var b: List[Bee] = currentPlace.bees
          while (b.nonEmpty && !hasKilledBee) {
            if (!b.head.isDead) {
              currentPlace.bees.head.armor_=(0)
              hasKilledBee = true
            }
            b = b.tail
          }
        }
        placesNotChecked = placesNotChecked.tail
      }
      if (hasKilledBee) turnAwait = 3
    }
  }
}

class BodyguardAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place], antInit: Option[Ant] = None)
  extends Ant(posX, posY, "weeds", colony, _place, cost = 4, container = true, _armor = 2, waterProof = true) {

  private var _ant: Option[Ant] = antInit

  def ant: Option[Ant] = _ant
  def canAddAnt: Boolean = ant.isEmpty

  def ant_=(modifiedAnt: Option[Ant]) {
    if (modifiedAnt.isDefined && ant.isDefined) throw new  IllegalArgumentException("It already contains an ant.")
    _ant = modifiedAnt
  }

  override def armor_=(newArmor: Int): Unit = {
    super.armor_=(newArmor)
    if (newArmor <= 0) {
      ant.get.armor_=(ant.get.armor + newArmor)
      place.get.removeAnt()
    }
  }

  override def moveActions(): Unit = {
    if (ant.isDefined) ant.get.moveActions()
  }
}
