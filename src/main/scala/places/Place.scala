package places

import insects._

class Place (private val name: String, posX: Int, posY: Int, entranceInit: Option[Place], exitInit: Option[Place]) {

  private var in: Option[Place] = entranceInit
  private var out: Option[Place] = exitInit
  private var _bees: List[Bee] = Nil
  private var _ant: Option[Ant] = None

  def x: Int = posX
  def y: Int = posY
  def entrance: Option[Place] = in
  def exit: Option[Place] = out

  def height: Int = 66
  def width: Int = 66
  def bees: List[Bee] = _bees
  def ant: Ant = _ant.get

  def entrance_=(newIn: Option[Place]) { in = newIn }
  def exit_=(newOut: Option[Place]) { out = newOut }

  def isBeesIn: Boolean = bees.nonEmpty
  def addBee(b: Bee): Unit = { _bees = b::_bees }
  def removeBee(b: Bee): Unit = {
    def rmBee(b2: Bee, l2: List[Bee]): List[Bee] = {
      l2 match {
        case Nil => throw new IllegalArgumentException("Bee not in the list of bees.")
        case h::t => if (b2 == h) t else h::rmBee(b2, t)
      }
    }
    _bees = rmBee(b, _bees)
  }

  def isAntIn: Boolean = _ant.isDefined

  def canAddAnt(a: Ant): Boolean = {
    if (a.isContainer) {
      !isAntIn || !ant.isContainer
    } else {
      !isAntIn || (ant.isContainer && ant.asInstanceOf[BodyguardAnt].canAddAnt)
    }
  }

  def addAnt(a: Ant): Unit = {
    require(canAddAnt(a))
    if (!isAntIn) _ant = Some(a)
    else {
      if (ant.isContainer) ant.asInstanceOf[BodyguardAnt].ant_=(Some(a))
      else {
        a.asInstanceOf[BodyguardAnt].ant_=(Some(ant))
        _ant = Some(a)
      }
    }
  }

  def removeAnt(): Unit = {
    require(_ant.isDefined)
    if (ant.isContainer) _ant = ant.asInstanceOf[BodyguardAnt].ant
    else if (!ant.isInstanceOf[QueenAnt]) _ant = None
  }
}

class WaterPlace(name: String, posX: Int, posY: Int, entranceInit: Option[Place], exitInit: Option[Place])
  extends Place(name, posX, posY, entranceInit, exitInit) {

  override def addAnt(a: Ant): Unit = {
    super.addAnt(a)
    a.armor_=(0)
  }
}
