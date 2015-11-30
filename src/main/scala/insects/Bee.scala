package insects

import places.Place
import projectiles.Projectile

class Bee(posX: Int, posY: Int, _place: Option[Place] = None, _armor: Int = 1)
  extends Insect(posX, posY, "bee", _place, _armor, true, damagesAmount = 1) {

  var hasGoneThrough = false

  if (place.isDefined) {
    place.get.addBee(this)
  }

  override def moveActions() {
    if (place.isDefined && place.get.isAntIn && place.get.ant.blocksPath) {
      val ant: Ant = place.get.ant
      new Projectile(x.toInt, y, ant, damages)
    }
  }

  /** Called each frame for the bees to move. */
  def move() {
    if (place.isDefined && (!place.get.isAntIn || !place.get.ant.blocksPath)) {
      if (place.get.exit.isDefined) moveTowardPlace(place.get.exit.get)
      else if (place.get.x + place.get.width < x) x_=(x - dx)
      else moveTowardEnd()
    }
  }

  /** Move toward a Place. */
  def moveTowardPlace(nextPlace: Place) {
    x_=(x - dx)
    if (nextPlace.x < x && x <= nextPlace.x + nextPlace.width) {
      place.get.removeBee(this)
      place_=(nextPlace)
      nextPlace.addBee(this)
      if (nextPlace.isAntIn && nextPlace.ant.isInstanceOf[QueenAnt]) hasGoneThrough = true  // The bees won
    }
  }

  /** The bee is in the last Place and move to the exit of it to eventually win. */
  def moveTowardEnd(): Unit = {
    x_=(x - dx)
    if (place.get.x - (place.get.width / 2) >= x) {
      hasGoneThrough = true
    }
  }
}

/** Bee that can hit from 2 Places or less. */
class RangeBee(posX: Int, posY: Int, _place: Option[Place] = None, _armor: Int = 1)
  extends Bee(posX, posY, _place, _armor) {

  override def moveActions(): Unit = {
    var currentPlace: Option[Place] = place.get.exit
    var i: Int = 2
    var hasHitAnt: Boolean = false
    while (i > 0 && currentPlace.isDefined && !hasHitAnt) {
      if (currentPlace.get.isAntIn) {
        hasHitAnt = true
        new Projectile(x.toInt, y, currentPlace.get.ant, damages)
      }
      i -= 1
      currentPlace = currentPlace.get.exit
    }
  }
}
