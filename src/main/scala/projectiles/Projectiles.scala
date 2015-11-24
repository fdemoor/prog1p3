package projectiles

object Projectiles {
  private var _projectiles: List[Projectile] = Nil

  def projectiles: List[Projectile] = _projectiles

  def addProjectile(newProjectile: Projectile) { _projectiles = newProjectile::_projectiles }

  def removeProjectile(oldProjectile: Projectile): Unit = {
    def removeFromList(l: List[Projectile], elt: Projectile): List[Projectile] = {
      l match {
        case Nil => Nil
        case x::xs => if (x == elt) xs
                      else x::removeFromList(xs, elt)
      }
    }
    removeFromList(_projectiles, oldProjectile)
  }

  def moves(): Unit = {
    for (p <- projectiles) {
      p.move()
      if (p.hasHit) removeProjectile(p)
    }
  }
}
