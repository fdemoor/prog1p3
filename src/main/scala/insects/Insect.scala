package insects

import javax.swing.ImageIcon

import places._

/**
  * Common behavior of the insects.
  *
  * @param posX x of the top left corner of the sprite.
  * @param posY y of the top right corner of the sprite.
  * @param img Image name of the sprite.
  * @param placeInit Initial Place.
  * @param armorInit Initial armor amount (=health points).
  * @param waterProof True if the insect can live in a water Place.
  * @param damagesAmount Initial amount of damages the insect can deal.
  */
abstract class Insect(posX: Int, posY: Int, img: String, placeInit: Option[Place], armorInit: Int = 1,
                      waterProof: Boolean = false, damagesAmount: Int = 0) {
  val icon: ImageIcon = new ImageIcon(getClass.getResource("/img/" + img + ".png"))
  val im = icon.getImage

  private var _x: Float = posX.toFloat
  private var _y: Float = posY.toFloat
  private var _dx: Float = 0.3f
  private var _dy: Float = 0
  private var _place: Option[Place] = placeInit
  private var _armor = armorInit
  val initialArmor: Int = armorInit
  private var _isDead = false
  private var doubledDamages: Boolean = false
  private var _damages: Int = damagesAmount
  private var _lvl: Int = 1

  def x: Float = _x
  def y: Float = _y
  def dx: Float = _dx
  def dy: Float = _dy
  def place: Option[Place] = _place
  def armor: Int = _armor
  def isDead: Boolean = _isDead
  def isWaterProof: Boolean = waterProof
  def hasDoubledDamages: Boolean = doubledDamages
  def lvl: Int = _lvl
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
  def unDoubleDamages() { doubledDamages = false }
  def upgradeDamages() {
    _damages = _damages * 2
    _lvl += 1
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
