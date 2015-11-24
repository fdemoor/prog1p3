package projectiles

object Projectiles {
  private var _projectiles: List[Projectile] = Nil

  def projectiles: List[Projectile] = _projectiles

  def addProjectile(newProjectile: Projectile) { _projectiles = newProjectile::_projectiles }

  def removeProjectile(oldProjectile: Projectile): Unit = {
    _projectiles = _projectiles.filter(_ != oldProjectile)
  }

  def moves(): Unit = {
    for (p <- projectiles) {
      p.move()
      if (p.hasHit) removeProjectile(p)
    }
  }
}
