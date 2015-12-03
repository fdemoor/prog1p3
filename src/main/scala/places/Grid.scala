package places

import javax.swing.ImageIcon

import scala.util.Random

/**
  * Manages the places of the board.
  * @param l Initial list of places.
  */
class Grid (l: List[Place]) {

  private var _places = l
  def places: List[Place] = _places


  /** Returns an array of the p tunnel entrances.
    * Creates a grid of n.p places, perWater is the probability percentage of water places.
    * @param n Number of Places in a tunnel (columns of the grid).
    * @param p Number of tunnels (rows of the grid).
    */
  def grid(n: Int, p: Int, perWater: Int): Array[Option[Place]] = {
    val rand = new Random()
    val tunnelEntrances: Array[Option[Place]] = (for (i <- 0 until p) yield None).toArray
    val iconPlace: ImageIcon = new ImageIcon(getClass.getResource("/img/tunnel.png"))

    for (i <- 0 until p) {
      var pl = new Place("Box"+i.toString+".0", 20, 100 + iconPlace.getIconHeight*i, None, None)
      this.add(pl)
      for (j <- 1 until n) {
        if (rand.nextInt(101) > perWater) {
          pl = new Place("Box"+i.toString+"."+j.toString, 20 + pl.width*j,
            100 + pl.height*i, None, Some(this.places.head))
        } else {
          pl = new WaterPlace("Box"+i.toString+"."+j.toString, 20 + pl.width*j,
            100 + pl.height*i, None, Some(this.places.head))
        }
        this.add(pl)
      }
      for (j <- 1 until n) {
        this.places(j).entrance_=(Some(this.places(j-1)))
      }
      tunnelEntrances(i) = Some(this.places.head)
    }
    tunnelEntrances
  }

  def add(p: Place): Unit = { _places = p::_places }
}
