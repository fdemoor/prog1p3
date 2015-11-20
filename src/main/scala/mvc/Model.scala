package mvc

import insects._, places._, colony._

class Model {
  var places: List[Place] = Nil
  val Colony: Colony = new Colony(2)

  val p = new Place ("Box0", 100, 100, None, None)
  places = p::places
  for (i <- 1 until 8) {
    val p = new Place ("Box" + i.toString, 100 + 66*i, 100, Some(places.head), None)
    places = p::places
  }
  for (i <- 1 to places.length -1) {
    places(i).exit_=(Some(places(i-1)))
  }


  def tryAddingAnt(cursorPos: (Int, Int), typeAnt: String): Unit = {
    def findPlaceAddingAnt(l: List[Place]): Unit = {
      l match {
        case Nil => ()
        case pl :: pls =>
          if (pl.x <= cursorPos._1 && cursorPos._1 < pl.x + 67 && pl.y <= cursorPos._2 && cursorPos._2 < pl.y + 67) {
            if (typeAnt == "harvester") {
              // Put new Harvester
              try {
                new HarvesterAnt(pl.x, pl.y, Colony, Some(pl))
              } catch {
                case ex: IllegalArgumentException => ()
              }
            }
          } else {
            findPlaceAddingAnt(pls)
          }
      }
    }
    findPlaceAddingAnt(places)
  }

  def moveActions(): Unit = {
    for (p <- places) {
      if (p.isAntIn) p.ant.moveActions()
      for (bee <- p.bees) bee.moveActions()
    }
  }
  def move(): Unit = {
    for (p <- places) {
      for (bee <- p.bees) bee.move()
    }
  }
}
