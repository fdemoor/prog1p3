package mvc

import colony.Colony, places.Place
import java.awt.{Color, Dimension, Graphics2D, geom}
import javax.swing.ImageIcon
import scala.swing.event._
import scala.swing.Panel

class View(_controller: Controller, placesList: List[Place], _Colony: Colony) {

  private val controller: Controller = _controller
  //private val places: List[Place] = placesList
  private val Colony: Colony = _Colony

  lazy val ui = new Panel {
    background = Color.white
    preferredSize = new Dimension(800, 600)
    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    /* Returns the current position of the mouse (or null if it's not over the panel */
    def getPos = peer.getMousePosition()

    /* VAR USER INTERFACE */
    val harvesterIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_harvester.png"))
    val harvesterIm = harvesterIcon.getImage

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      val pos = getPos
      if (pos != null) g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)

      g.setColor(Color.black)

      for (p <- placesList) {
        val box = new geom.GeneralPath
        box.moveTo(p.x, p.y)
        box.lineTo(p.x + p.width,  p.y)
        box.lineTo(p.x + p.width,  p.y + p.height)
        box.lineTo(p.x, p.y + p.height)
        box.lineTo(p.x, p.y)
        g.draw(box)
        g.drawImage(p.im, p.x, p.y, peer)
        for (bee <- p.bees) {
          g.drawImage(bee.im, bee.x, bee.y, peer)
        }
        if (p.isAntIn) {
          g.drawImage(p.ant.im, p.ant.x, p.ant.y, peer)
        }
      }

      /* DRAW USER INTERFACE */
      g.drawString("Food: " + Colony.foodAmount.toString, 10, size.height-10)

      val harvesterSelectBox = new geom.GeneralPath
      harvesterSelectBox.moveTo(10, 10)
      harvesterSelectBox.lineTo(10 + harvesterIcon.getIconWidth, 10)
      harvesterSelectBox.lineTo(10 + harvesterIcon.getIconWidth,  10 + harvesterIcon.getIconHeight + 40)
      harvesterSelectBox.lineTo(10, 10 + harvesterIcon.getIconHeight + 40)
      harvesterSelectBox.lineTo(10, 10)
      if (controller.harvesterSelected) {
        g.setColor(Color.red)
      } else {
        g.setColor(Color.black)
      }
      g.drawString(" Cost: 2", 10, 30)
      g.drawString(" Armor: 1", 10, 50)
      g.draw(harvesterSelectBox)
      g.drawImage(harvesterIm, 10, 50, peer)
    }


    /* USER ACTIONS */
    reactions += {
      case e: MousePressed =>
      case e: MouseDragged  =>
      case e: MouseReleased =>
        if (10 <= getPos.x && getPos.x < 10 + harvesterIcon.getIconWidth + 50 &&
            10 <= getPos.y && getPos.y < 10 + harvesterIcon.getIconHeight + 50) {
          controller.harvesterClicked() // Harvester Box
        } else {
          controller.placeClicked((getPos.x, getPos.y))
        }
      case KeyTyped(_, 'c', _, _) =>
      case KeyTyped(_, 'i', _, _) =>
      case KeyTyped(_, 'a', _, _) =>
      case KeyTyped(_, 'z', _, _) =>
      case _: FocusLost => repaint()
    }
  }

  def getUI = ui
  def repaint() { ui.repaint() }
}
