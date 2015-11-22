package insects

import places.Place

class Bee(posX: Int, posY: Int, _place: Option[Place] = None, _armor: Int = 1)
  extends Insect(posX, posY, "bee", _place, _armor, true, damagesAmount = 1) {

  var hasGoneThrough = false

  if (place.isDefined) {
    place.get.addBee(this)
  }

  override def moveActions() {
    if (place.isDefined && place.get.isAntIn && place.get.ant.blocksPath) {
      val ant: Ant = place.get.ant
      ant.armor_=(ant.armor - damages)
    }
  }

  /** Called each frame for the bees to move. */
  def move() {
    if (place.isDefined && (!place.get.isAntIn || !place.get.ant.blocksPath)) {
      if (place.get.exit.isDefined) moveTowardPlace(place.get.exit.get)
      else moveTowardEnd()
    }
  }

  def moveTowardPlace(nextPlace: Place) {
    y_=(y - dy)
    if (nextPlace.x < x && x <= nextPlace.x + nextPlace.width) {
      place.get.removeBee(this)
      place_=(nextPlace)
      nextPlace.addBee(this)
      if (nextPlace.isAntIn && nextPlace.ant.isInstanceOf[QueenAnt]) hasGoneThrough = true  // The bees won
    }
  }

  def moveTowardEnd(): Unit = {
    y_=(y - dy)
    if (place.get.x - (place.get.width / 2) == x) {
      hasGoneThrough = true
    }
  }
}
