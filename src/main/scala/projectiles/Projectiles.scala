package projectiles

/** Stores and manages the projectiles. */
object Projectiles {

  private var _projectiles: List[Projectile] = Nil
  def projectiles: List[Projectile] = _projectiles

  def addProjectile(newProjectile: Projectile) { _projectiles = newProjectile::_projectiles }

  def removeProjectile(oldProjectile: Projectile): Unit = {
    _projectiles = _projectiles.filter(_ != oldProjectile)
  }

  def removeUselessProjectiles(): Unit = {
    def aux(l: List[Projectile]): List[Projectile] = {
      if (l.isEmpty) l
      else {
        if (l.head.hasHit || l.head.target.isDead) aux(l.tail)
        else l.head::aux(l.tail)
      }
    }
    _projectiles = aux(_projectiles)
  }

  def moves(): Unit = {
    for (p <- projectiles) {
      p.move()
      if (p.hasHit) removeProjectile(p)
    }
  }
}
