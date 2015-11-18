// This demonstrates the major Scala Swing functionalities that we need in this project


import places._
import scala.swing._
import scala.swing.{ SimpleSwingApplication, MainFrame, Panel }
import scala.swing.event._
import java.awt.event.{ ActionEvent, ActionListener }
import java.awt.{ Color, Graphics2D, Point, geom, MouseInfo }
import javax.swing.{ ImageIcon, Timer }

// That object is your application
object Main extends SimpleSwingApplication {
    
    private var places_list: List[Place] = Nil
    private var insects_list: List[Insect] = Nil
    
    def get_places () = places_list
    def get_insects () = insects_list
    
    
    
    
}



object UI {

  lazy val ui = new Panel {
      
    background = Color.white
    preferredSize = new Dimension(800, 600)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed =>
      case e: MouseDragged  =>
      case e: MouseReleased =>
      case KeyTyped(_, 'c', _, _) => 
      case KeyTyped(_, 'i', _, _) =>
      case KeyTyped(_, 'a', _, _) =>
      case KeyTyped(_, 'z', _, _) =>

      case _: FocusLost => repaint()
    }

    /* Returns the current position of the mouse (or null if it's not over the panel */
    def getPos() = peer.getMousePosition()

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      val pos = getPos()
      if (pos != null)
        g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)

      g.setColor(Color.black)
      
      for (p <- Main.get_places) {
          val box = new geom.GeneralPath
          box.moveTo( p.x, p.y )
          box.lineTo( p.x + 66,  p.y )
          box.lineTo( p.x + 66,  p.y + 66 )
          box.lineTo( p.x, p.y + 66 )
          box.lineTo( p.x, p.y )
          g.draw(box)
          
      for (i <- Main.get_insects) {
        g.drawImage(i.im, i.x, i.y, peer)
      }
    }
  }

}


  class MyTimer extends ActionListener {
    /* Configuration */
    val fpsTarget = 50 // Desired amount of frames per second
    var delay = 1000 / fpsTarget

    /* The swing timer */
    val timer = new Timer(delay, this)
    timer.setCoalesce(true) // Please restart by yourself
    timer.start()           // Let's go

    /* react to the timer events */
    def actionPerformed(e: ActionEvent): Unit = {
      UI.ui.repaint() // Tell Scala that the image should be redrawn
    }
  }
  val t = new MyTimer()

  // Part 4: Main initialization: Create a new window and populate it
  //////////////////////////////
  def top = new MainFrame {
    title = "Simple Demo Application"
    contents = UI.ui
  }
}
