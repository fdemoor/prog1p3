package mvc

import javax.swing.ImageIcon

import insects._, places._, colony._, projectiles._

class Model {
  private var _places: List[Place] = Nil
  private val _Colony: Colony = new Colony(2)
//  private var _projectiles: List[Projectile] = Nil


  def places: List[Place] = _places
  def Colony: Colony = _Colony
//  def projectiles: List[Projectile] = _projectiles

  /* Initializing places. */
  val iconPlace: ImageIcon = new ImageIcon(getClass.getResource("/img/tunnel.png"))
  val p = new Place ("Box0", 20, 120, None, None)

  _places = p::_places
  for (i <- 1 until 7) {
    val p = new Place ("Box" + i.toString, 20 + iconPlace.getIconWidth*i, 120, Some(_places.head), None)
    _places = p::_places
  }
  _places = new WaterPlace ("Box7", 20 + iconPlace.getIconWidth*7, 120, Some(_places.head), None)::_places
  for (i <- 1 to _places.length -1) {
    _places(i).exit_=(Some(_places(i-1)))
  }

  // ONLY FOR TEST //
  val beeTest: Bee = new Bee(800, 120, Some(_places.head), 1)
  //  val proj: Projectile = new Projectile(20, 120, beeTest, 2)
  //  _projectiles = proj::_projectiles
  // ------------ //

  /** A place has been clicked, find it and (eventually) add the ant. */
  def tryAddingAnt(cursorPos: (Int, Int), typeAnt: String): Unit = {
    def findPlaceAddingAnt(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
              pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
                
            if (typeAnt == "harvester") {
              // Put new Harvester
              try {
                new HarvesterAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "shortThrower") {
              // Put new Harvester
              try {
                new ShortThrower(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "longThrower") {
              // Put new Harvester
              try {
                new LongThrower(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "fire") {
              // Put new Harvester
              try {
                new FireAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "scuba") {
              // Put new Harvester
              try {
                new ScubaAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "wall") {
              // Put new Harvester
              try {
                new WallAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "ninja") {
              // Put new Harvester
              try {
                new NinjaAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "hungry") {
              // Put new Harvester
              try {
                new HungryAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "queen") {
              // Put new Harvester
              try {
                new QueenAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              } 
            } else if (typeAnt == "bodyGuard") {
              // Put new Harvester
              try {
                if (pl.isAntIn) new BodyguardAnt(pl.x, pl.y,_Colony, Some(p), Some(pl.ant))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            }
            
            
          } else {
            findPlaceAddingAnt(pls)
          }
      }
    }
    findPlaceAddingAnt(_places)
  }

  /** Remove an ant when Bye Box is selected */
  def tryRemovingAnt(cursorPos: (Int, Int)): Unit = {
    def findPlaceRemovingAnt(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
              pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
              if (pl.isAntIn) pl.removeAnt()
          } else {
            findPlaceRemovingAnt(pls)
          }
      }
    }
    findPlaceRemovingAnt(_places)
  }

  def moveActionsAnts(): Unit = {
    for (p <- _places) {
      if (p.isAntIn) p.ant.moveActions()
    }
  }
  def moveActionsBees(): Unit = {
    for (p <- _places) {
      for (bee <- p.bees) bee.moveActions()
    }
  }
  def moveActionsProjectiles(): Unit = {
//    for (proj <- _projectiles) {
//      proj.move()
////      proj.hit()
//    }
    Projectiles.moves()
  }
  def removeDeads(): Unit = {
    for (p <- _places) {
      if (p.isAntIn && p.ant.isDead) p.removeAnt()
      def removeDeadBees(bL: List[Bee]): Unit = {
        if (bL.nonEmpty) {
          if (bL.head.isDead) p.removeBee(bL.head)
          removeDeadBees(bL.tail)
        }
      }
      removeDeadBees(p.bees)
    }
  }
  def move(): Unit = {
    for (p <- _places) {
      for (bee <- p.bees) bee.move()
    }
  }
}
