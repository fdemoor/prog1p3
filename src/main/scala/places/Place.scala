package places

import javax.swing.ImageIcon

import insects._

/**
  * A box in a tunnel.
  *
  * It can contain at max 1 ant and multiple bees.
  * @param name Name of the place, should be unique for a better textual visualization.
  * @param posX x of the top left corner of the Place.
  * @param posY y of the top left corner of the Place.
  * @param entranceInit Place on the right of this one, None if it is on the extreme right.
  * @param exitInit Place on the left of this one, None if it is on the extreme left.
  */
class Place (private val name: String, posX: Int, posY: Int, entranceInit: Option[Place], exitInit: Option[Place]) {
  var icon: ImageIcon = new ImageIcon(getClass.getResource("/img/tunnel.png"))
  var im = icon.getImage

  private var in: Option[Place] = entranceInit  // Place on the right of this one
  private var out: Option[Place] = exitInit  // Place on the left of this one
  private var _bees: List[Bee] = Nil
  private var _ant: Option[Ant] = None
  private var _isFrozen = false
  private var frozenTurnsLeft = 0

  def x: Int = posX
  def y: Int = posY
  def entrance: Option[Place] = in
  def exit: Option[Place] = out
  def height: Int = icon.getIconHeight
  def width: Int = icon.getIconWidth
  def bees: List[Bee] = _bees
  def ant: Ant = _ant.get
  def isFrozen = _isFrozen
  override def toString = name

  def entrance_=(newIn: Option[Place]) { in = newIn }
  def exit_=(newOut: Option[Place]) { out = newOut }

  def isBeesIn: Boolean = bees.nonEmpty
  def addBee(b: Bee): Unit = { _bees = b::_bees }
  def removeBee(b: Bee): Unit = {
    def rmBee(b2: Bee, l2: List[Bee]): List[Bee] = {
      if (l2.isEmpty) throw new IllegalArgumentException("Bee not in the list of bees.")
      if (b2 == l2.head) return l2.tail
      l2.head::rmBee(b2, l2.tail)
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
        a.asInstanceOf[BodyguardAnt].ant_=(_ant)
        _ant = Some(a)
      }
    }
  }

  def removeAnt(): Unit = {
    require(_ant.isDefined)
    if (ant.isContainer) _ant = ant.asInstanceOf[BodyguardAnt].ant
    else if (!ant.isInstanceOf[QueenAnt]) _ant = None
  }

  /** Should be called only to remove a queen when there is already one in the tunnel.*/
  def removeQueen(): Unit = {
    if (ant.isContainer) ant.asInstanceOf[BodyguardAnt].ant_=(None)
    else _ant = None
  }

  /* Freeze power */
  def freeze(nbTurns: Int) = {
    _isFrozen = true
    frozenTurnsLeft = nbTurns
  }
  def freezeDecr() = {
    frozenTurnsLeft = frozenTurnsLeft-1
    if (frozenTurnsLeft == 0) _isFrozen = false
  }
}

/**
  * Place where only waterproof ants can live.
  *
  * @param name Name of the place, should be unique for a better textual visualization.
  * @param posX x of the top left corner of the Place.
  * @param posY y of the top left corner of the Place.
  * @param entranceInit Place on the right of this one, None if it is on the extreme right.
  * @param exitInit Place on the left of this one, None if it is on the extreme left.
  */
class WaterPlace(name: String, posX: Int, posY: Int, entranceInit: Option[Place], exitInit: Option[Place])
  extends Place(name, posX, posY, entranceInit, exitInit) {

  icon = new ImageIcon(getClass.getResource("/img/tunnel_water.png"))
  im = icon.getImage

  override def addAnt(a: Ant): Unit = {
    super.addAnt(a)
    if (isAntIn && !a.isWaterProof) {
      a.armor_=(0)
      removeAnt()
    }
  }
}

