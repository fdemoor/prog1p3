import insects._
import places._
import colony._
import scala.swing._
import scala.swing.{ SimpleSwingApplication, MainFrame, Panel }
import scala.swing.event._
import java.awt.event.{ ActionEvent, ActionListener }
import java.awt.{ Color, Graphics2D, Point, geom, MouseInfo }
import javax.swing.{ ImageIcon, Timer }


object Main extends SimpleSwingApplication {

  private var places_list: List[Place] = Nil
  private var insects_list: List[Insect] = Nil
  private val Colony: Colony = new Colony(2)

  def places = places_list
  def insects = insects_list


  for (i <- 0 until 8) {
    val p = new Place ("Box" + i.toString, 100 + 66*i, 100, None, None)
    places_list = p::places_list
  }

  val i = new HarvesterAnt(100, 100, Colony, Some(places_list.head))
  insects_list = i::insects_list

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
    def getPos = peer.getMousePosition()

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      val pos = getPos
      if (pos != null)
        g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)

      g.setColor(Color.black)

      for (p <- Main.places) {
        val box = new geom.GeneralPath
        box.moveTo( p.x, p.y )
        box.lineTo( p.x + 66,  p.y )
        box.lineTo( p.x + 66,  p.y + 66 )
        box.lineTo( p.x, p.y + 66 )
        box.lineTo( p.x, p.y )
        g.draw(box)
      }

      for (i <- Main.insects) {
        g.drawImage(i.im, i.x, i.y, peer)
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
      ui.repaint() // Tell Scala that the image should be redrawn
      for (ant <- insects) {
        ant.moveActions()
      }
      print(Colony.foodAmount, " ")
    }
  }

  val t = new MyTimer()

  // Part 4: Main initialization: Create a new window and populate it
  //////////////////////////////
  def top = new MainFrame {
    title = "Simple Demo Application"
    contents = ui
  }

}
