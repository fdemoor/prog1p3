package mvc

import javax.swing.ImageIcon
import util.Random

import insects._, places._, colony._, projectiles._

class Model {
  //private var _places: List[Place] = Nil
  private val _Colony: Colony = new Colony(2)
//  private var _projectiles: List[Projectile] = Nil


  //def places: List[Place] = _places
  def Colony: Colony = _Colony
//  def projectiles: List[Projectile] = _projectiles

  /* Initializing places. */
  val iconPlace: ImageIcon = new ImageIcon(getClass.getResource("/img/tunnel.png"))

  val gridWidth: Int = 8
  val gridHeight: Int = 5

  val gridGame = new Grid(Nil: List[Place])



  /** Create a grid of nXp places, perWater is the probability percentage of water places
   *  Return an array of the p tunnel entrances */
  def grid(n: Int, p: Int, perWater: Int): Array[Option[Place]] = {

    val alea = new Random()
    val tunnelEntrances: Array[Option[Place]] = (for (i <- 0 until p) yield None).toArray

    for (i <- 0 until p) {

      var p = new Place("Box"+i.toString+".0", 20, 120 + iconPlace.getIconHeight*i, None, None)
      gridGame.add(p)
     // _places = p::_places

      for (j <- 1 until n) {
        if (alea.nextInt(101) > perWater) {
          p = new Place("Box"+i.toString+"."+j.toString, 20 + iconPlace.getIconWidth*j,
                120 + iconPlace.getIconHeight*i, None, Some(gridGame.places.head))
        } else {
          p = new WaterPlace("Box"+i.toString+"."+j.toString, 20 + iconPlace.getIconWidth*j,
                120 + iconPlace.getIconHeight*i, None, Some(gridGame.places.head))
        }
        gridGame.add(p)
        //_places = p::_places
      }

      for (j <- 1 until n) {
        gridGame.places(j).entrance_=(Some(gridGame.places(j-1)))
      }

      tunnelEntrances(i) = Some(gridGame.places.head)
    }
    tunnelEntrances
  }

  val tunnelEntrances: Array[Option[Place]] = grid(gridWidth, gridHeight, 15)
  val aleaWave = new Random()

  def beeWave(): Unit = {
    val choice: Int = aleaWave.nextInt(gridHeight)
    new Bee(800, 120 + iconPlace.getIconHeight*choice,
      tunnelEntrances(choice), 3 + _Colony.foodAmount%4)
  }



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
                new BodyguardAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            }


          } else {
            findPlaceAddingAnt(pls)
          }
      }
    }
    findPlaceAddingAnt(gridGame.places)
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
    findPlaceRemovingAnt(gridGame.places)
  }

  def moveActionsAnts(): Unit = {
    for (p <- gridGame.places) {
      if (p.isAntIn) p.ant.moveActions()
    }
  }
  def moveActionsBees(): Unit = {
    for (p <- gridGame.places) {
      for (bee <- p.bees) bee.moveActions()
    }
  }
  def moveActionsProjectiles(): Unit = {
    Projectiles.moves()
  }
  def removeDeads(): Unit = {
    for (p <- gridGame.places) {
      if (p.isAntIn && p.ant.isDead) p.removeAnt()
      def removeDeadBees(bL: List[Bee]): Unit = {
        if (bL.nonEmpty) {
          if (bL.head.isDead) {p.removeBee(bL.head); _Colony.incrScoreAmount(5)}
          removeDeadBees(bL.tail)
        }
      }
      removeDeadBees(p.bees)
    }
  }
  def move(): Unit = {
    for (p <- gridGame.places) {
      for (bee <- p.bees) bee.move()
    }
  }
}
