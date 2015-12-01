package insects

import javax.swing.ImageIcon

import places._

abstract class Insect(posX: Int, posY: Int, img: String, placeInit: Option[Place], armorInit: Int = 1,
                      waterProof: Boolean = false, damagesAmount: Int = 0) {
  val icon: ImageIcon = new ImageIcon(getClass.getResource("/img/" + img + ".png"))
  val im = icon.getImage

  private var _x: Float = posX.toFloat
  private var _y: Int = posY
  private var _dx: Float = 0.3f
  private var _dy: Int = 0
  private var _place: Option[Place] = placeInit
  private var _armor = armorInit
  private var _isDead = false
  private var doubledDamages: Boolean = false
  private var _damages: Int = damagesAmount
  private var _damagesUpgraded: Boolean = false

  def x: Float = _x
  def y: Int = _y
  def dx: Float = _dx
  def dy: Int = _dy
  def place: Option[Place] = _place
  def armor: Int = _armor
  def isDead: Boolean = _isDead
  def isWaterProof: Boolean = waterProof
  def hasDoubledDamages: Boolean = doubledDamages
  def hasDamagesUpgraded: Boolean = _damagesUpgraded
  def damages: Int = {
    if (hasDoubledDamages) 2 * _damages
    else _damages
  }

  def x_=(newX: Float) {
    _x = newX
  }
  def y_=(newY: Int) {
    _y = newY
  }
  def place_=(newPlace: Place) { _place = Some(newPlace) }
  def kill() = { _isDead = true}
  def armor_=(newArmor: Int) {
    _armor = newArmor
    if (_armor <= 0) kill()
  }
  def damages_=(newD: Int) { _damages = newD }
  def doubleDamages() { doubledDamages = true }
  def unDoubleDamages() { doubledDamages = false}
  def upgradeDamages() {
    _damagesUpgraded = true
    _damages = _damages * 2
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
  def moveActions(): Unit
}
