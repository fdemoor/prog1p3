package mvc

import javax.swing.ImageIcon
import util.Random

import insects._, places._, colony._, projectiles._

class Model {

  /* Initializing a new colony with 2 as starting food amount */
  private val _Colony: Colony = new Colony(2)
  def Colony: Colony = _Colony


  /* Initializing places. */
  val iconPlace: ImageIcon = new ImageIcon(getClass.getResource("/img/tunnel.png"))
  val gridWidth: Int = 8
  val gridHeight: Int = 5
  val gridGame = new Grid(Nil: List[Place])
  val tunnelEntrances: Array[Option[Place]] = gridGame.grid(gridWidth, gridHeight, 15)
  
  
  /** Initialize a new bee in a randomly choosen tunnel */
  val aleaWave = new Random()
  def beeWave(): Unit = {
    val choice: Int = aleaWave.nextInt(gridHeight)
    new Bee(800, 120 + iconPlace.getIconHeight*choice,
      tunnelEntrances(choice), 3)
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
              try {
                new HarvesterAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "shortThrower") {
              try {
                new ShortThrower(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "longThrower") {
              try {
                new LongThrower(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "fire") {
              try {
                new FireAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "scuba") {
              try {
                new ScubaAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "wall") {
              try {
                new WallAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "ninja") {
              try {
                new NinjaAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "hungry") {
              try {
                new HungryAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "queen") {
              try {
                new QueenAnt(pl.x, pl.y, _Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            } else if (typeAnt == "bodyGuard") {
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


  /** Execute move actions for all ants */
  def moveActionsAnts(): Unit = {
    for (p <- gridGame.places) {
      if (p.isAntIn) p.ant.moveActions()
    }
  }
  
  
  /** Execute move actions for all bees */
  def moveActionsBees(): Unit = {
    for (p <- gridGame.places) {
      for (bee <- p.bees) bee.moveActions()
    }
  }
  
  
  /** Execute move actions for all projectiles */
  def moveActionsProjectiles(): Unit = {
    Projectiles.moves()
  }
  
  
  /** Remove dead insects */
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
  
  
  /** Moving all bees */
  def move(): Unit = {
    for (p <- gridGame.places) {
      for (bee <- p.bees) bee.move()
    }
  }
}
