package projectiles

import insects._

class Projectile (posX: Int, posY: Int, target: Bee) {
  
  private var _x: Int = posX
  private var _y: Int = posY
  
  def x: Int = _x
  def y: Int = _y
  
  private var _dx: Int = 1
  private var _dy: Int = 0
  
  def move() = {
    _x += _dx
    _y += _dy
  }
  
  def hit() = {
    if (_x == target.x && _y == target.y) {
      target.armor_=(target.armor - 1)
      //TODO this = None how to remove ??
    }
  }
  
  
}
