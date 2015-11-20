package mvc

import insects._, places._, colony._

class Model {
  private var _places: List[Place] = Nil
  private val _Colony: Colony = new Colony(2)

  def places: List[Place] = _places
  def Colony: Colony = _Colony

  /* Initializing places. */
  val p = new Place ("Box0", 100, 100, None, None)
  _places = p::_places
  for (i <- 1 until 8) {
    val p = new Place ("Box" + i.toString, 100 + 66*i, 100, Some(_places.head), None)
    _places = p::_places
  }
  for (i <- 1 to _places.length -1) {
    _places(i).exit_=(Some(_places(i-1)))
  }

  /** A place has been clicked, find it and (eventually) add the ant. */
  def tryAddingAnt(cursorPos: (Int, Int), typeAnt: String): Unit = {
    def findPlaceAddingAnt(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + 67 && pl.y <= cursorPos._2 && cursorPos._2 < pl.y + 67) {
            if (typeAnt == "harvester") {
              // Put new Harvester
              try {
                new HarvesterAnt(pl.x, pl.y, _Colony, Some(pl))
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

  def moveActions(): Unit = {
    for (p <- _places) {
      if (p.isAntIn) p.ant.moveActions()
      for (bee <- p.bees) bee.moveActions()
    }
  }
  def move(): Unit = {
    for (p <- _places) {
      for (bee <- p.bees) bee.move()
    }
  }
}
