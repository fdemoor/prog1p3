package places

import insects._


class Place (name: String, posx: Int, posy: Int, entranceInit: Option[Place], exitInit: Option[Place]) {

  def x: Int = posx
  def y: Int = posy

  private val in: Place = entranceInit.get
  private val out: Place = exitInit.get
  
  private var bees: List[Bee] = Nil
  private var ant: Option[Ant] = None

  def entrance: Place = in
  def exit: Place = out

  def getHeight: Int = 66
  def getWidth: Int = 66
  def getBees: List[Bee] = bees
  def getAnt: Ant = ant.get
  
  def isBeesIn : Boolean = bees.isEmpty
  def addBee(b: Bee): Unit = bees = b::bees
  def removeBee(b: Bee): Unit = {
    bees match {
      case Nil => ()
      case h::t =>
        if (b != h) removeBee(b)
    }
  }
  
  def isAntIn: Boolean = ant.isDefined
  
  def addAnt(a: Ant): Unit = {
    assert(ant.isEmpty)
    ant = Some(a)
  }

  def removeAnt(): Unit = {
    assert(ant.isDefined)
    ant = None
  }
  
}
