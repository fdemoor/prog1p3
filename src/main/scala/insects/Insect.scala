package insects

import javax.swing.ImageIcon

class Insect(posx: Int, posy: Int, img: String) {
  val icon: ImageIcon = new ImageIcon("src/main/resources/img/" + img + ".png")
  val im = icon.getImage()

  private var varx: Int = posx
  private var vary: Int = posy
  private var vardx: Int = 1
  private var vardy: Int = 1

  def x(): Int = { varx }
  def y(): Int = { vary }
  def dx(): Int = { vardx }
  def dy(): Int = { vardy }

    // Update the position considering speed
  def move (): Unit = {
        varx += this.dx
        vary += this.dy
    }

    // Increase speed
  def accelerate (ax:Int, ay:Int): Unit = {
    vardx += ax
    vardy += ay
  }

    // Decrease speed
  def decelerate (ax:Int, ay:Int): Unit = {
    vardx -= ax
    vardy -= ay
  }
}

class Ant() extends Insect(1, 1, "fourmi") {
  val hello = "hello"
}