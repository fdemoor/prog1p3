package insects

import javax.swing.ImageIcon
import places._

abstract class Insect(posX: Int, posY: Int, img: String, placeInit: Option[Place], armorInit: Int = 1
                      , waterProof: Boolean = false, damagesAmount: Int = 0) {
  val icon: ImageIcon = new ImageIcon(getClass.getResource("/img/" + img + ".png"))
  val im = icon.getImage

  private var _x: Int = posX
  private var _y: Int = posY
  private var _dx: Int = 1
  private var _dy: Int = 1
  private var _place: Option[Place] = placeInit
  private var _armor = armorInit
  private var _isDead = false
  private var doubledDamages: Boolean = false
  private val _damages: Int = damagesAmount

  def x: Int = _x
  def y: Int = _y
  def dx: Int = _dx
  def dy: Int = _dy
  def place: Option[Place] = _place
  def armor: Int = _armor
  def isDead: Boolean = _isDead
  def isWaterProof: Boolean = waterProof
  def hasDoubledDamages: Boolean = doubledDamages
  def damages: Int = {
    if (hasDoubledDamages) 2 * _damages
    else _damages
  }

  def x_=(newX: Int) {
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
  def doubleDamages() { doubledDamages = true }

//  /** Update the position considering speed */
//  def move() {
//    _x += _dx
//    _y += _dy
//  }

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
