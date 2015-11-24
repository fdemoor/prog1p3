package projectiles

import insects._

class Projectile (posX: Int, posY: Int, target: Insect, damages: Int) {

  Projectiles.addProjectile(this)

  private var _x: Int = posX
  private var _y: Int = posY
  private var _hasHit: Boolean = false

  def x: Int = _x
  def y: Int = _y
  def hasHit: Boolean = _hasHit

  private var _dx: Int = 1
  private var _dy: Int = 0

  def move() = {
    if (x < target.x) _x += _dx
    else _x -= _dx
    _y += _dy
    hit()
  }

  def hit() = {
    if (_x == target.x && _y == target.y) {
      target.armor_=(target.armor - damages)
      _hasHit = true
    }
  }
}
