package mvc

import colony.Colony
import places.Place


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
    var harvesterSelected: Boolean = false
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
        box.moveTo( p.x, p.y )
        box.lineTo( p.x + 66,  p.y )
        box.lineTo( p.x + 66,  p.y + 66 )
        box.lineTo( p.x, p.y + 66 )
        box.lineTo( p.x, p.y )
        g.draw(box)
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
      harvesterSelectBox.moveTo( 10, 10 )
      harvesterSelectBox.lineTo( 10 + 66, 10 )
      harvesterSelectBox.lineTo( 10 + 66,  10 + 66 )
      harvesterSelectBox.lineTo( 10, 10 + 66 )
      harvesterSelectBox.lineTo( 10, 10 )
      if (harvesterSelected) {
        g.setColor(Color.red)
      } else {
        g.setColor(Color.black)
      }
      g.draw(harvesterSelectBox)
      g.drawImage(harvesterIm, 10, 10, peer)
    }

    def harvesterClicked() { harvesterSelected = !harvesterSelected }


    /* USER ACTIONS */



    reactions += {
      case e: MousePressed =>
      case e: MouseDragged  =>
      case e: MouseReleased =>
        if (10 <= getPos.x && getPos.x < 77 && 10 <= getPos.y && getPos.y < 77) {
          controller.harvesterClicked() // Harvester Box
          harvesterClicked()
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

  def repaint() { ui.repaint() }

  def getUI = ui
}
