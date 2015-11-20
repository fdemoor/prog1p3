package places

import insects._

class Place (private val name: String, posX: Int, posY: Int, entranceInit: Option[Place], exitInit: Option[Place]) {

  private val in: Option[Place] = entranceInit
  private val out: Option[Place] = exitInit
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

  def isBeesIn: Boolean = _bees.isEmpty
  def addBee(b: Bee): Unit = { _bees = b::_bees }
  def removeBee(b: Bee): Unit = {
    def rmBee(b2: Bee, l2: List[Bee]): List[Bee] = {
      l2 match {
        case h::t => if (b2 != h) h::rmBee(b2, t) else t
        case Nil => throw new IllegalArgumentException("Bee not in the list of bees.")
      }
    }
    _bees = rmBee(b, _bees)
  }

  def isAntIn: Boolean = _ant.isDefined

  def addAnt(a: Ant): Unit = {
    require(_ant.isEmpty)
    _ant = Some(a)
  }

  def removeAnt(): Unit = {
    require(_ant.isDefined)
    _ant = None
  }

}
