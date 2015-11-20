package insects

import javax.swing.ImageIcon
import colony._
import places._

abstract class Insect(posX: Int, posY: Int, img: String, placeInit: Option[Place], armorInit: Int = 1
                      , waterProof: Boolean = false) {
  val icon: ImageIcon = new ImageIcon(getClass.getResource("/img/" + img + ".png"))
  val im = icon.getImage

  private var _x: Int = posX
  private var _y: Int = posY
  private var _dx: Int = 1
  private var _dy: Int = 1
  private var _place: Option[Place] = placeInit
  private var _armor = armorInit
  private var _isDead = false

  def x: Int = _x
  def y: Int = _y
  def dx: Int = _dx
  def dy: Int = _dy
  def place: Option[Place] = _place
  def armor: Int = _armor
  def isDead: Boolean = _isDead
  def isWaterProof: Boolean = waterProof

  def x_=(newX: Int) {
    _x = newX
  }
  def y_=(newY: Int) {
    _y = newY
  }
  def place_=(newPlace: Place) { _place = Some(newPlace) }
  def kill() = { _isDead = true}
  def armor_=(newArmor: Int) {
    _armor = newArmor
    if (_armor <= 0) kill() // main has to check after a turn which insects are DE4D
  }

//  /** Update the position considering speed */
//  def move() {
//    _x += _dx
//    _y += _dy
//  }

  /** Increase speed */
  def accelerate(ax:Int, ay:Int) {
    _dx += ax
    _dy += ay
  }

  /** Decrease speed */
  def decelerate(ax:Int, ay:Int) {
    _dx -= ax
    _dy -= ay
  }

  /** Called each move for each insect to do its actions. */
  def moveActions(): Unit
}

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Option[Place], cost: Int, _armor: Int = 1)
  extends Insect(posX, posY, "ant_" + img, _place, _armor) {

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

class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Option[Place])
  extends Ant(posX, posY, "thrower", colony, _place, 2) {

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

class Bee(posX: Int, posY: Int, _place: Option[Place] = None, _armor: Int = 1)
  extends Insect(posX, posY, "bee", _place, _armor, true) {

  var hasGoneThrough = false

  if (place.isDefined) {
    place.get.addBee(this)
  }

  override def moveActions() {
    if (place.isDefined && place.get.isAntIn) {
      val ant: Ant = place.get.ant
      ant.armor_=(ant.armor - 1)
    }
  }

  /** Called each frame for the bees to move. */
  def move() {
    if (place.isDefined && !place.get.isAntIn) {
      if (place.get.exit.isDefined) moveTowardPlace(place.get.exit.get)
      else moveTowardEnd()
    }
  }

  def moveTowardPlace(nextPlace: Place) {
    y_=(y - dy)
    if (nextPlace.x - (nextPlace.width / 2) < x && x <= nextPlace.x + (nextPlace.width / 2)) {
      //|| nextPlace.y - (nextPlace.height / 2) < y && y <= nextPlace.y + (nextPlace.height / 2)) {
      place.get.removeBee(this)
      place_=(nextPlace)
      nextPlace.addBee(this)
    }
  }

  def moveTowardEnd(): Unit = {
    y_=(y - dy)
    if (place.get.x - (place.get.width / 2) == x) {
      hasGoneThrough = true
    }
  }
}
