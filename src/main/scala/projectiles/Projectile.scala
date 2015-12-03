package projectiles

import insects._

/**
  * Projectile made by an Insect to attack another one.
  *
  * @param posX x of the top left corner of the sprite of the Projectile.
  * @param posY y of the top left corner of the sprite of the Projectile.
  * @param target Insect targeted by the Projectile.
  * @param damages Damage amount that should be inflicted on hit.
  */
class Projectile (posX: Int, posY: Int, val target: Insect, damages: Int) {
  // If insects didn't have coordinates projectiles should be event-based
  // and it would be easy for the View to get a new target for the projectile if the current one dies.

  Projectiles.addProjectile(this)

  private var _x: Int = posX
  private var _y: Int = posY
  private var _hasHit: Boolean = false

  def x: Int = _x
  def y: Int = _y
  def hasHit: Boolean = _hasHit

  private var _dx: Int = 3
  private var _dy: Int = 0

  /** Called each frame for the Projectile to move. */
  def move() = {
    if (x < target.x) _x += _dx
    else _x -= _dx
    _y += _dy
    hit()
  }

  /** Tests if the Projectile has hit its target and deals damages if so. */
  def hit() = {
    if (x >= target.x && x <= target.x + target.icon.getIconWidth &&
        y >= target.y && y <= target.y + target.icon.getIconHeight) {
      target.armor_=(target.armor - damages)
      _hasHit = true
    }
  }
}
