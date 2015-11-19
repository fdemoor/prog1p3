package insects

import javax.swing.ImageIcon
import colony._
import places._

class Insect(posX: Int, posY: Int, img: String, _place: Place, _armor: Int = 1) {
  val icon: ImageIcon = new ImageIcon(getClass.getResource("/img/" + img + ".png"))
  val im = icon.getImage

  private var _x: Int = posX
  private var _y: Int = posY
  private var _dx: Int = 1
  private var _dy: Int = 1
  private var place: Place = _place
  private var armor = _armor
  private var dead = false

  def getX: Int = _x
  def x: Int = _x
  def getY: Int = _y
  def y: Int = _y
  def getDX: Int = _dx
  def dx: Int = _dx
  def getDY: Int = _dy
  def dy: Int = _dy
  def getPlace: Place = place
  def getArmor: Int = armor
  def isDead: Boolean = dead

  def setX(newX: Int) {
    _x = newX
  }
  def setY(newY: Int) {
    _y = newY
  }
  def setPlace(newPlace: Place) { place = newPlace }
  
  def setArmor(newArmor: Int) {
    armor = newArmor
    if (armor >= 0) dead = true // main has to check after a turn which insects are DE4D
  }

  /** Update the position considering speed */
  def move() {
    _x += _dx
    _y += _dy
  }

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
  def moveActions(): Unit = {()}
}

//class Ant(posX: Int, posY: Int, img: String, colony: Colony, _place: Place, cost: Int, _armor: Int = 1)
  //extends Insect(posX, posY, "ant_" + img, _place, _armor) {

  //private val Cost = cost
  //private val Colony = colony

  //assert(Colony.getFoodAmount >= Cost)

  //def getCost: Int = Cost
  //def getColony: Colony = Colony

  //def addFood(amount: Int = 1) { Colony.setFoodAmount(Colony.getFoodAmount + amount) }
//}

//class HarvesterAnt(posX: Int, posY: Int, colony: Colony, _place: Place)
  //extends Ant(posX, posY, "harvester", colony, _place, 2) {

  //override def moveActions() {
    //addFood()
  //}
//}

//class ThrowerAnt(posX: Int, posY: Int, colony: Colony, _place: Place)
  //extends Ant(posX, posY, "thrower", colony, _place, 2) {

  //override def moveActions() {
    //if (getPlace.isBeesIn){
      //for (bee: Bee <- getPlace.getBees) {
        //bee.setArmor(bee.getArmor - 1)
      //}
    //}
  //}
//}

//class Bee(posX: Int, posY: Int, _place: Place = None, _armor: Int = 1)
  //extends Insect(posX, posY, "bee", _place, _armor) {

  //override def moveActions() {
    //if (getPlace.isDefined && getPlace.isAntIn) {
      //val ant: Ant = getPlace.getAnt
      //ant.setArmor(ant.getArmor - 1)
    //}
  //}

  ///** Called each frame for the bees to move. */
  //def move() {
    //if (getPlace.isDefined && !(getPlace.isAntIn)) {  // Not quite sure how to manage None/Some things
      //moveTowardPlace(getPlace.getNextPlace)
    //}
  //}

  //def moveTowardPlace(nextPlace: Place) {
    //setY(getY - getDY)
    //// move toward the next place NOT REALLY IMPLEMENTED YET?
    //if (nextPlace.x - (nextPlace.getWidth / 2) < getX && getX <= nextPlace.x + (nextPlace.getWidth / 2) ||
      //nextPlace.y - (nextPlace.getHeight / 2) < getY && getY <= nextPlace.y + (nextPlace.getHeight / 2)) {

      //getPlace.removeBee(this)
      //setPlace(nextPlace)
      //nextPlace.addBee(this)
    //}
  //}
//}
