import scala.swing._
import scala.swing.{ SimpleSwingApplication, MainFrame, Panel }
import scala.swing.event._
import java.awt.event.{ ActionEvent, ActionListener }
import java.awt.{ Color, Graphics2D, Point, geom, MouseInfo }
import javax.swing.{ ImageIcon, Timer }


class Insect(posX: Int, posY: Int, img: String, _place: Place, _armor: Int = 1) {
  val icon: ImageIcon = new ImageIcon("../resources/img/" + img + ".png")
  val im = icon.getImage

  private var _x: Int = posX
  private var _y: Int = posY
  private var _dx: Int = 1
  private var _dy: Int = 1
  private var place: Place = _place
  private var armor = _armor
  private var dead = false

  def getX: Int = _x
  def x: Int = _x
  def getY: Int = _y
  def y: Int = _y
  def getDX: Int = _dx
  def dx: Int = _dx
  def getDY: Int = _dy
  def dy: Int = _dy
  def getPlace: Place = place
  def getArmor: Int = armor
  def isDead: Boolean = dead

  def setX(newX: Int) {
    _x = newX
  }
  def setY(newY: Int) {
    _y = newY
  }
  def setPlace(newPlace: Place) { place = newPlace }
  
  def setArmor(newArmor: Int) {
    armor = newArmor
    if (armor >= 0) dead = true // main has to check after a turn which insects are DE4D
  }

  /** Update the position considering speed */
  def move() {
    _x += _dx
    _y += _dy
  }

  /** Increase speed */
  def accelerate(ax:Int, ay:Int) {
    _dx += ax
    _dy += ay
  }

  /** Decrease speed */
  def decelerate(ax:Int, ay:Int) {
    _dx -= ax
    _dy -= ay
  }

  /** Called each move for each insect to do its actions. */
  def moveActions(): Unit = {()}
}

class Place (name: String, posx: Int, posy: Int) {
	
	def x (): Int = posx
	def y (): Int = posy
	
	private var in: Place = this
	private var out: Place = this
	
	def entrance (p: Place): Unit = { in = p }
	def exit (p: Place): Unit = { out = p }
	
	def getHeihgt (): Int = 66
	def getWidth (): Int = 66
	
} 

object Test extends SimpleSwingApplication{
    
    private var places_list: List[Place] = Nil
    private var insects_list: List[Insect] = Nil
    
    def get_places () = places_list
    def get_insects () = insects_list
    
    for (i <- 0 until 8) {
		val p = new Place ("Box" + i.toString, 100 + 66*i, 100)
		places_list = p::(places_list)
	}
	
	val i = new Insect (100, 100, "ant_thrower", places_list(0))
	insects_list = i::(insects_list)
    

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
      
      for (p <- Test.get_places) {
          val box = new geom.GeneralPath
          box.moveTo( p.x, p.y )
          box.lineTo( p.x + 66,  p.y )
          box.lineTo( p.x + 66,  p.y + 66 )
          box.lineTo( p.x, p.y + 66 )
          box.lineTo( p.x, p.y )
          g.draw(box)
          
      for (i <- Test.get_insects) {
		  g.drawImage(i.im, i.getX, i.getY, peer)
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
      ui.repaint() // Tell Scala that the image should be redrawn
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
