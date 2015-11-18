package insects

import javax.swing.ImageIcon
import colony._

abstract class Insect(posX: Int, posY: Int, img: String, _armor: Int = 1) {
  val icon: ImageIcon = new ImageIcon("src/main/resources/img/" + img + ".png")
  val im = icon.getImage

  private var x: Int = posX
  private var y: Int = posY
  private var dx: Int = 1
  private var dy: Int = 1
  private var armor = _armor

  def getX: Int = x
  def getY: Int = y
  def getDX: Int = dx
  def getDY: Int = dy
  def getArmor: Int = armor

  def setX(newX: Int) { x = newX }
  def setY(newY: Int) { y = newY }
  def setArmor(newArmor: Int) {
    armor = newArmor
    if (armor >= 0) this = null
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

  def moveActions()
}

abstract class Ant(posX: Int, posY: Int, img: String, colony: Colony, cost: Int, _armor: Int = 1)
  extends Insect(posX, posY, "ant_" + img, _armor) {

  private val Cost = cost
  private val Colony = colony

  def getCost: Int = Cost
  def getColonny: Colony = Colony

  def addFood(amount: Int = 1) { Colony.setFoodAmount(Colony.getFoodAmount + amount) }
}

class HarvesterAnt(posX: Int, posY: Int, colony: Colony) extends Ant(posX, posY, "harvester", colony, 2) {
  override def moveActions() {
   addFood()
  }
}

class ThrowerAnt(posX: Int, posY: Int, colony: Colony) extends Ant(posX, posY, "harvester", colony, 2) {
  override def moveActions() {
   addFood()
  }
}
