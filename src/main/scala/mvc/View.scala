package mvc

import java.awt.{Color, Dimension, Graphics2D}
import javax.swing.ImageIcon

import colony.Colony
import insects.BodyguardAnt
import places._
import projectiles.Projectiles

import scala.swing.Panel
import scala.swing.event._

case class ClickFound() extends Exception

/**
  * Controls the UI of the game.
  */
class View(_controller: Controller, grid: Grid, _Colony: Colony) {

  private val controller: Controller = _controller
  private val Colony: Colony = _Colony

  val freezeOverlay: ImageIcon = new ImageIcon(getClass.getResource("/img/freeze_overlay.png"))
  val radarOverlay: ImageIcon = new ImageIcon(getClass.getResource("/img/radar_overlay.png"))

  /** Paint the places and all that is inside */
  def paintGrid(gridGame: Grid, g: Graphics2D, peer:java.awt.Component): Unit = {
    for (p <- gridGame.places) {
      g.setColor(Color.black)
      g.drawRect(p.x, p.y, p.width, p.height)
      g.drawImage(p.im, p.x, p.y, peer)
      if (p.isFrozen) g.drawImage(freezeOverlay.getImage, p.x, p.y, peer)

      for (bee <- p.bees) {
        val xtoCenter: Int = (p.width - bee.icon.getIconWidth) / 2
        val ytoCenter: Int = (p.height - bee.icon.getIconHeight) / 2
        g.drawImage(bee.im, bee.x.toInt + xtoCenter, bee.y.toInt + ytoCenter, peer)
        g.setColor(Color.black)
        var toPrint: String = ""
        if (bee.isVisible)
          toPrint = "lvl"+bee.lvl.toString+" "+bee.armor.toString+"/"+bee.initialArmor.toString
        else toPrint = "lvl"+bee.lvl.toString
        g.drawString(toPrint, bee.x + xtoCenter, bee.y + ytoCenter*2)
      }
      if (p.isAntIn) { // TODO : a bodyguard is in p.ant, and it screws up drawing
        val xtoCenter: Int = (p.width - p.ant.icon.getIconWidth) / 2
        val ytoCenter: Int = (p.height - p.ant.icon.getIconHeight) / 2
        if (p.ant.isContainer && p.ant.asInstanceOf[BodyguardAnt].ant.isDefined) {
          val underAnt = p.ant.asInstanceOf[BodyguardAnt].ant.get
          g.drawImage(underAnt.im, underAnt.x.toInt + xtoCenter, underAnt.y.toInt + ytoCenter, peer)
        }
        if (p.ant.hasRadar) g.drawImage(radarOverlay.getImage, p.x, p.y, peer)
        g.drawImage(p.ant.im, p.ant.x.toInt + xtoCenter, p.ant.y.toInt + ytoCenter, peer)
        g.setColor(Color.white)
        g.drawString("lvl"+p.ant.lvl.toString, p.ant.x, p.ant.y + p.height)
      }
    }
  }

  /* INFO MESSAGE */
  object Msg {
    var _msg = ""
    var turnsLeft = 0

    def msg: String = _msg

    def init(): Unit = {_msg = ""}

    def isEmpty: Boolean = _msg == ""

    def decr(): Unit = {
      turnsLeft -= 1
      if (turnsLeft <= 0) this.init()
    }

    def setMsg(newMsg: String): Unit = {
      _msg = newMsg
      turnsLeft = 2 // print the message during 2 turns
    }
  }

  def getMsg = Msg



  lazy val ui = new Panel {
    background = Color.white
    preferredSize = new Dimension(900, 700)
    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)


    /* Returns the current position of the mouse (or null if it's not over the panel */
    def getPos = peer.getMousePosition()


    /* CREATING MENU */
    val menu = new UIButtons(Nil: List[UIButton])

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

    val bodyguardIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_weeds.png"))
    val bodyguardButton = new UIButton(bodyguardIcon, 10 + menu.buttons.head.width*9, 10, 6 ,2, "bodyguard")
    menu.add(bodyguardButton)

    val rmIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/remover.png"))
    val rmButton = new UIButtonRM(rmIcon, 10 + menu.buttons.head.width*10, 10)
    menu.add(rmButton)


    /* CREATING POWER BUTTONS */

    val freezeIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/freeze.png"))
    val freezeButton = new UIButtonFreeze(freezeIcon, 10, 700-100, controller.freezeCost)
    menu.add(freezeButton)

    val radarIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/radar.png"))
    val radarButton = new UIButtonRadar(radarIcon, 10 + menu.buttons.head.width*1, 700-100, controller.radarCost)
    menu.add(radarButton)

    val doubleIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/double.png"))
    val doubleButton = new UIButtonDouble(doubleIcon, 10 + menu.buttons.head.width*2, 700-100, controller.doubleCost)
    menu.add(doubleButton)



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
      g.drawString(getMsg.msg, 10+48+40+40, size.height-10)

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
          case ex: NotEnoughFood => getMsg.setMsg("YOU DON'T HAVE ENOUGH FOOD !!!")
          case ex: ClickFound => ()
        }
      case _: FocusLost => repaint()
    }
  }

  def getUI = ui
  def repaint() { ui.repaint() }
}
