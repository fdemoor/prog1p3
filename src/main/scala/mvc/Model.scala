package mvc

import javax.swing.ImageIcon

import colony._
import insects._
import places._
import projectiles._

import scala.util.Random

case class NotEnoughFood() extends Exception
case class NotEmptyPlace() extends Exception

/**
  * Manage the elements of the game.
  */
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


  /* Initialize power costs */
  val freezeCost: Int = 10
  val radarCost: Int = 10
  val doubleCost: Int = 10


  /** Initialize a new bee in a randomly chosen tunnel */
  val randWave = new Random()
  def beeWave(beeLvl: Int, waveNumber: Int): Unit = {
    val choice: Int = randWave.nextInt(gridHeight)
    if (waveNumber%2 == 0) {
      val b = new RangeBee(800, 100 + iconPlace.getIconHeight*choice, tunnelEntrances(choice), 3*beeLvl)
      if (beeLvl > 5) b.upgradeDamages()
    } else {
      val b = new Bee(800, 100 + iconPlace.getIconHeight*choice, tunnelEntrances(choice), 3*beeLvl)
      if (beeLvl > 5) b.upgradeDamages()
    }
  }


  /** A place has been clicked, find it and (eventually) add the ant. */
  def tryAddingAnt(cursorPos: (Int, Int), typeAnt: String): Unit = {
    def findPlaceAddingAnt(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
              pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
            try {
              typeAnt match {
                case "harvester" => new HarvesterAnt(pl.x, pl.y, _Colony, Some(pl))
                case "shortThrower" => new ShortThrowerAnt(pl.x, pl.y, _Colony, Some(pl))
                case "longThrower" => new LongThrowerAnt(pl.x, pl.y, _Colony, Some(pl))
                case "fire" => new FireAnt(pl.x, pl.y, _Colony, Some(pl))
                case "scuba" => new ScubaAnt(pl.x, pl.y, _Colony, Some(pl))
                case "wall" => new WallAnt(pl.x, pl.y, _Colony, Some(pl))
                case "ninja" => new NinjaAnt(pl.x, pl.y, _Colony, Some(pl))
                case "hungry" => new HungryAnt(pl.x, pl.y, _Colony, Some(pl))
                case "queen" => new QueenAnt(pl.x, pl.y, _Colony, Some(pl))
                case "bodyguard" => new BodyguardAnt(pl.x, pl.y, _Colony, Some(pl))
              }
//              /* Create a new ant according to the type given. */
//              val args = Array(pl.x, pl.y, _Colony, Some(pl)).asInstanceOf[Array[AnyRef]]
//              Class.forName("insects." + typeAnt.capitalize + "Ant").getConstructors()(0).newInstance(args:_*)
              // The commented code above doesn't work because it can't deal with optional arguments in constructors
              // It is the scuba ant that have optional arguments
            } catch {
//              case ex: java.lang.reflect.InvocationTargetException =>
//                if (ex.getCause.getMessage == "Not enough food.") throw NotEnoughFood() else throw ex
              case ex: IllegalArgumentException =>
                if (ex.getMessage == "Not enough food.") throw NotEnoughFood()
                else if (ex.getMessage == "There is already an ant.") throw NotEmptyPlace()
                else throw ex
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


  /** Try to freeze a place */
  def tryFreezing(cursorPos: (Int, Int)): Unit = {
    def findPlaceFreezing(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
              pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
              pl.freeze(3)
          } else {
            findPlaceFreezing(pls)
          }
      }
    }
    if (_Colony.foodAmount >= freezeCost) {
      findPlaceFreezing(gridGame.places)
      _Colony.foodAmount_=(_Colony.foodAmount-freezeCost)
    } else throw NotEnoughFood()
  }


  /** Try to give an ant radar power */
  def tryRadar(cursorPos: (Int, Int)): Unit = {
    def findPlace(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
            pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
            if (pl.isAntIn &&
              ((pl.ant.isContainer && pl.ant.asInstanceOf[BodyguardAnt].ant.isDefined) || !pl.ant.isContainer))
              pl.ant.addRadar()
          } else {
            findPlace(pls)
          }
      }
    }
    if (_Colony.foodAmount >= radarCost) {
      findPlace(gridGame.places)
      _Colony.foodAmount_=(_Colony.foodAmount-radarCost)
    } else throw NotEnoughFood()
  }


  /** Try to give an ant double damage power */
  def tryDouble(cursorPos: (Int, Int)): Unit = {
    def findPlace(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + iconPlace.getIconWidth &&
            pl.y <= cursorPos._2 && cursorPos._2 < pl.y + iconPlace.getIconHeight) {
            if (pl.isAntIn &&
              ((pl.ant.isContainer && pl.ant.asInstanceOf[BodyguardAnt].ant.isDefined) || !pl.ant.isContainer))
              pl.ant.upgradeDamages()
          } else {
            findPlace(pls)
          }
      }
    }
    if (_Colony.foodAmount >= doubleCost) {
      findPlace(gridGame.places)
      _Colony.foodAmount_=(_Colony.foodAmount-doubleCost)
    } else throw NotEnoughFood()
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
      if (p.isFrozen) p.freezeDecr()
      for (bee <- p.bees) bee.moveActions()
    }
  }


  /** Execute move actions for all projectiles */
  def moveActionsProjectiles(): Unit = {
    Projectiles.moves()
    Projectiles.removeUselessProjectiles()
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
    for (p <- gridGame.places; bee <- p.bees)
        if (!p.isFrozen) bee.move()
  }

  def isEnded: Boolean = {
    for (p <- gridGame.places; bee <- p.bees) if (bee.hasGoneThrough) return true
    false
  }
}
