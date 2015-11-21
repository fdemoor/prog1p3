package insects

import places.Place

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
