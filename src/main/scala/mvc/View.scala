package mvc

import colony.Colony, places._, projectiles.Projectiles
import java.awt.{Color, Dimension, Graphics2D, geom}
import javax.swing.ImageIcon
import insects.BodyguardAnt

import scala.swing.event._
import scala.swing.Panel

case class ClickFound() extends Exception


class View(_controller: Controller, grid: Grid, _Colony: Colony) {

  private val controller: Controller = _controller
  private val Colony: Colony = _Colony
  
  
  /** Paint the places and all that is inside */
  def paintGrid(gridGame: Grid, g: Graphics2D, peer:java.awt.Component): Unit = {
    
    for (p <- gridGame.places) {
      g.setColor(Color.black)
      if (p.isFrozen) g.setColor(Color.blue)
      else g.setColor(Color.black)
      g.drawRect(p.x, p.y, p.width, p.height)
      g.drawImage(p.im, p.x, p.y, peer)
      
      for (bee <- p.bees) {
        val xtoCenter: Int = (p.width - bee.icon.getIconWidth) / 2
        val ytoCenter: Int = (p.height - bee.icon.getIconHeight) / 2
        g.drawImage(bee.im, bee.x.toInt + xtoCenter, bee.y + ytoCenter, peer)
      }
      if (p.isAntIn) {
        val xtoCenter: Int = (p.width - p.ant.icon.getIconWidth) / 2
        val ytoCenter: Int = (p.height - p.ant.icon.getIconHeight) / 2
        if (p.ant.isContainer && p.ant.asInstanceOf[BodyguardAnt].ant.isDefined) {
          val underAnt = p.ant.asInstanceOf[BodyguardAnt].ant.get
          g.drawImage(underAnt.im, underAnt.x.toInt + xtoCenter, underAnt.y + ytoCenter, peer)
        }
        g.drawImage(p.ant.im, p.ant.x.toInt + xtoCenter, p.ant.y + ytoCenter, peer)
      }
    }
  }
  

  lazy val ui = new Panel {
    
    background = Color.white
    preferredSize = new Dimension(900, 700)
    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)
    

    /* Returns the current position of the mouse (or null if it's not over the panel */
    def getPos = peer.getMousePosition()


    /* CREATING MENU */
    val menu = new UIButtonMenu(Nil: List[UIButton])

    val harvesterIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_harvester.png"))
    val harvesterButton = new UIButton(harvesterIcon, 10, 10, 2, 1, "harvester")
    menu.add(harvesterButton)

    val shortThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_shortthrower.png"))
    val shortThrowerButton = new UIButton(shortThrowerIcon, 10 + menu.buttons.head.width, 10, 3 ,1, "shortThrower")
    menu.add(shortThrowerButton)
    
    val longThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_longthrower.png"))
    val longThrowerButton = new UIButton(longThrowerIcon, 10 + menu.buttons.head.width*2, 10, 3 ,1, "longThrower")
    menu.add(longThrowerButton)
    
    val fireIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_fire.png"))
    val fireButton = new UIButton(fireIcon, 10 + menu.buttons.head.width*3, 10, 5 ,1, "fire")
    menu.add(fireButton)
    
    val scubaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_scuba.png"))
    val scubaButton = new UIButton(scubaIcon, 10 + menu.buttons.head.width*4, 10, 5 ,1, "scuba")
    menu.add(scubaButton)
    
    val wallIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_wall.png"))
    val wallButton = new UIButton(wallIcon, 10 + menu.buttons.head.width*5, 10, 4 ,4, "wall")
    menu.add(wallButton)
    
    val ninjaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_ninja.png"))
    val ninjaButton = new UIButton(ninjaIcon, 10 + menu.buttons.head.width*6, 10, 6 ,1, "ninja")
    menu.add(ninjaButton)
    
    val hungryIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_hungry.png"))
    val hungryButton = new UIButton(hungryIcon, 10 + menu.buttons.head.width*7, 10, 4 ,1, "hungry")
    menu.add(hungryButton)
    
    val queenIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_queen.png"))
    val queenButton = new UIButton(queenIcon, 10 + menu.buttons.head.width*8, 10, 6 ,2, "queen")
    menu.add(queenButton)
    
    val bodyGuardIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_weeds.png"))
    val bodyGuardButton = new UIButton(bodyGuardIcon, 10 + menu.buttons.head.width*9, 10, 6 ,2, "bodyGuard")
    menu.add(bodyGuardButton)
    
    val rmIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/remover.png"))
    val rmButton = new UIButtonRM(rmIcon, 10 + menu.buttons.head.width*10, 10)
    menu.add(rmButton)
    
    
    /* CREATING POWER BUTTONS */
    
    val freezeIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/freeze.png"))
    val freezeButton = new UIButtonFreeze(freezeIcon, 10, 700-100, controller.freezeCost()) // TODO
    menu.add(freezeButton)
    
    /*val radarIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/freeze.png"))
    val radarButton = new UIButtonRadar(radarIcon, 10 + menu.buttons.head.width*1, 700-100, controller.radarCost)
    menu.add(radarButton)
    
    // DECOMMENTING THIS MAKE SCALA GO CRAZY HELP TODO
    
    val doubleIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/double.png"))
    val doubleButton = new UIButtonDouble(doubleIcon, 10 + menu.buttons.head.width*2, 700-100, controller.doubleCost)
    menu.add(doubleButton)*/
    
    
    /* INFO MESSAGE */
    object Msg {
    
      var msg_ = ""
      def msg(): String = msg_
      
      def init(): Unit = {msg_ = ""}
      
      def isEmpty(): Boolean = {msg_ == ""}
      
      var turnsLeft = 0 // print the message during 2 turns
      def decr(): Unit = {
        turnsLeft -= 1
        if (turnsLeft <= 0) this.init()
      }
      
      def setMsg(newMsg: String): Unit = {
        msg_ = newMsg
        turnsLeft = 2
      }
    }


    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      //g.setColor(new Color(100, 100, 100))
      val pos = getPos
      if (pos != null) g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)


      /* BUTTON MENU */
      menu.paint(g, peer)


      /* PLACES */
      paintGrid(grid, g, peer)


      /* BOTTOM INFO BAR */
      
      g.setColor(Color.lightGray)
      g.fillRect(0, size.height-28, size.width, 28)
      g.setColor(Color.black)
      g.drawLine(0, size.height-28, size.width, size.height-28)
      
      // FOOD
      val foodIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/food.png"))
      g.drawImage(foodIcon.getImage, 10, size.height-26, peer)
      g.drawString(" " + Colony.foodAmount.toString, 10+16, size.height-10)
      
      // SCORE
      val scoreIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/score.png"))
      g.drawImage(scoreIcon.getImage, 10+32+40, size.height-26, peer)
      g.drawString(" " + Colony.scoreAmount.toString, 10+48+40, size.height-10)
      
      // INFO MESSAGE
      g.setColor(Color.black)
      g.drawString(Msg.msg, 10+48+40+40, size.height-10)
      


      /* PROJECTILES */
      g.setColor(Color.red)
      for (p <- Projectiles.projectiles) {
        g.fillOval(p.x, p.y + 30, 8, 8)
      }
    }


    /* USER ACTIONS */
    reactions += {
      case e: MouseReleased =>
        try {
          menu.mouseAction(getPos.x, getPos.y)
          controller.placeClicked((getPos.x, getPos.y), menu)
        } catch {
          case ex: NotEnoughFood => Msg.setMsg("YOU DON'T HAVE ENOUGH FOOD !!!")
          case ex: ClickFound => ()
        }
      case _: FocusLost => repaint()
    }
  }

  def getUI = ui
  def repaint() { ui.repaint() }
}
