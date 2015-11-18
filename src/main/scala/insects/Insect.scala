package insects

import javax.swing.ImageIcon
import colony._
import places._

abstract class Insect(posX: Int, posY: Int, img: String, _place: Place, _armor: Int = 1) {
  val icon: ImageIcon = new ImageIcon("src/main/resources/img/" + img + ".png")
  val im = icon.getImage

  private var x: Int = posX
  private var y: Int = posY
  private var dx: Int = 1
  private var dy: Int = 1
  private var place: Place = _place
  private var armor = _armor
  private var dead = false

  def getX: Int = x
  def getY: Int = y
  def getDX: Int = dx
  def getDY: Int = dy
  def getPlace: Place = place
  def getArmor: Int = armor
  def isDead: Boolean = dead

  def setX(newX: Int) { x = newX }
  def setY(newY: Int) { y = newY }
  def setPlace(newPlace: Place) { place = newPlace }
  def setArmor(newArmor: Int) {
    armor = newArmor
    if (armor >= 0) dead = true // main has to check after a turn which insects are DE4D
  }

  /** Update the position considering speed */
  def move() {
    x += dx
    y += dy
  }

  /** Increase speed */
  def accelerate(ax:Int, ay:Int) {
    dx += ax
    dy += ay
  }

  /** Decrease speed */
  def decelerate(ax:Int, ay:Int) {
    dx -= ax
    dy -= ay
  }

  /** Called each move for each insect to do its actions. */
  def moveActions()
}

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Place, cost: Int, _armor: Int = 1)
  extends Insect(posX, posY, "ant_" + img, _place, _armor) {

  private val Cost = cost
  private val Colony = colony

  assert(Colony.getFoodAmount >= Cost)

  def getCost: Int = Cost
  def getColony: Colony = Colony

  def addFood(amount: Int = 1) { Colony.setFoodAmount(Colony.getFoodAmount + amount) }
}

class HarvesterAnt(posX: Int, posY: Int, colony: Colony, _place: Place)
  extends Ant(posX, posY, "harvester", colony, _place, 2) {

  override def moveActions() {
    addFood()
  }
}

class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Place)
  extends Ant(posX, posY, "thrower", colony, _place, 2) {

  override def moveActions() {
    if (getPlace.isBeesIn){
      for (bee: Bee <- getPlace.getBees) {
        bee.setArmor(bee.getArmor - 1)
      }
    }
  }
}

class Bee(posX: Int, posY: Int, _place: Place = None, _armor: Int = 1)
  extends Insect(posX, posY, "bee", _place, _armor) {

  override def moveActions(): Unit = {
    if (getPlace.isDefined) {
      if (getPlace.isAntIn) {
        val ant: Ant = getPlace.getAnt
        ant.setArmor(ant.getArmor - 1)
      }
      else {
        getPlace.nextPlace
      }
    }
  }
}
