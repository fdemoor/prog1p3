package places

import javax.swing.ImageIcon
import scala.util.Random

class Grid (l: List[Place]) { // TODO maybe object instead of class ?

  private var places_ = l
  def places: List[Place] = places_


  /** Create a grid of nXp places, perWater is the probability percentage of water places
    *  Return an array of the p tunnel entrances */
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

  def add(p: Place): Unit = {places_ = p::places_}
}
