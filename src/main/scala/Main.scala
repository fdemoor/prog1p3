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
  private val Colony: Colony = new Colony(2)


  def places = places_list

  val p = new Place ("Box0", 100, 100, None, None)
    places_list = p::places_list
  for (i <- 1 until 8) {
    val p = new Place ("Box" + i.toString, 100 + 66*i, 100, Some(places_list.head), None)
    places_list = p::places_list
  }
  for (i <- 1 to places_list.length -1) {
    places_list(i).exit_=(Some(places_list(i-1)))
  }


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


      /* USER ACTIONS */

      def findPlace(l: List[Place]): Unit = {
        l match {
          case Nil => ()
          case pl::pls =>
            if (pl.x <= getPos.x && getPos.x < pl.x + 67 && pl.y <= getPos.y && getPos.y < pl.y + 67) {
              if (harvesterSelected) { // Put new Harvester
                try {
                  new HarvesterAnt(pl.x, pl.y, Colony, Some(pl))
                } catch {
                  case ex: IllegalArgumentException => ()
                }
              }
            } else {
              findPlace(pls)
            }
        }
      }

      reactions += {
        case e: MousePressed =>
        case e: MouseDragged  =>
        case e: MouseReleased =>

          if (10 <= getPos.x && getPos.x < 77 && 10 <= getPos.y && getPos.y < 77) {
            harvesterSelected = !harvesterSelected // Harvester Box
          } else {
              findPlace(places_list)
            }

        case KeyTyped(_, 'c', _, _) =>
        case KeyTyped(_, 'i', _, _) =>
        case KeyTyped(_, 'a', _, _) =>
        case KeyTyped(_, 'z', _, _) =>

        case _: FocusLost => repaint()
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
      for (p <- places_list) {
        if (p.isAntIn) p.ant.moveActions()
      }
      for (bee <- p.bees) bee.moveActions()

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
