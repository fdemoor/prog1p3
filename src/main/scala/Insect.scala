import scala.swing._
import scala.swing.{ SimpleSwingApplication, MainFrame, Panel }
import scala.swing.event._
import java.awt.event.{ ActionEvent, ActionListener }
import java.awt.{ Color, Graphics2D, Point, geom, MouseInfo }
import javax.swing.{ ImageIcon, Timer }

class insect (posx:Int, posy:Int) {
	
	val icon:ImageIcon = new ImageIcon("src/main/resources/img/bee.png")
    val im = icon.getImage( )
    
	private var varx:Int = posx
	private var vary:Int = posy
	private var vardx:Int = 1
	private var vardy:Int = 1
	
	def x(): Int = { varx}
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
	
		// remove one insect
	def del(): Unit = { //this.??
	}
	
		// remove all insects
	def reset(): Unit = {
	}
    
			
	
}
