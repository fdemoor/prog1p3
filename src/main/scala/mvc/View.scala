package mvc

import colony.Colony, places.Place, projectiles.Projectiles
import java.awt.{Color, Dimension, Graphics2D, geom}
import javax.swing.ImageIcon
import insects.BodyguardAnt

import scala.swing.event._
import scala.swing.Panel


class UIButton(icon: ImageIcon, posX: Int, posY: Int, cost: Int, armor: Int) {
  
  def x = posX
  def y = posY
  
  val armorIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/armor.png"))
  val foodIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/food.png"))
  
  private var isSelected_ = false
  val isSelected: Boolean = isSelected_
  def select() = {isSelected_ = true}
  def init() = {isSelected_ = false}
  
  val width: Int = 66
  val height: Int = 66 + 16
  
  def isClicked(getX: Int, getY: Int): Boolean = {
    (x <= getX && getX < x + width &&
      y <= getY && getY < y + height)
  }

  
  def paint(g: Graphics2D, peer:java.awt.Component): Unit = {
    
    // Food and armor information
    g.drawImage(armorIcon.getImage, x, y, peer)
    g.drawString(" "+armor.toString, x + armorIcon.getIconWidth, y + armorIcon.getIconHeight)
    g.drawImage(foodIcon.getImage, x + + armorIcon.getIconWidth*2, y, peer)
    g.drawString(" "+cost.toString, x + foodIcon.getIconWidth +
      armorIcon.getIconWidth*2, y + foodIcon.getIconHeight)
    
    if (isSelected) {
      g.setColor(Color.red)
    } else {
      g.setColor(Color.black)
    }
    g.drawRect(x, y, width, height)
    
    g.drawImage(icon.getImage, x, y + armorIcon.getIconHeight, peer)
  }
  
  
  
}

class UIButtonMenu (l: List[UIButton]) {

  val buttons: List[UIButton] = l

  def init(): Unit = {
      for (b <- buttons) b.init()
  }

  def mouseAction(getX: Int, getY: Int): Unit = {
    for (b <- buttons) {
      if (b.isClicked(getX, getY)) {
        init()
        b.select()
      }
    }
  }
  
}




class View(_controller: Controller, placesList: List[Place], _Colony: Colony) {

  private val controller: Controller = _controller
  //private val places: List[Place] = placesList
  private val Colony: Colony = _Colony

  lazy val ui = new Panel {
    background = Color.white
    preferredSize = new Dimension(900, 700)
    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    /* Returns the current position of the mouse (or null if it's not over the panel */
    def getPos = peer.getMousePosition()

    /* ICONS OF SELECTING BOXES */
    val harvesterIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_harvester.png"))
    val shortThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_shortthrower.png"))
    val longThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_longthrower.png"))
    val fireIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_fire.png"))
    val scubaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_scuba.png"))
    val wallIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_wall.png"))
    val ninjaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_ninja.png"))
    val hungryIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_hungry.png"))
    val queenIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_queen.png"))
    val bodyGuardIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_weeds.png"))
    val byeIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/remover.png"))


    val harvesterButton = new UIButton(harvesterIcon, 10, 10, 2, 1)
    
    
    

    val selectBoxWidth: Int = 66
    val selectBoxHeight: Int = 66 + 40


    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      val pos = getPos
      if (pos != null) g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)


      harvesterButton.paint(g, peer)

      // DRAW PLACES AND INSECTS //
      g.setColor(Color.black)
      for (p <- placesList) {
        val box = new geom.GeneralPath
        box.moveTo(p.x, p.y)
        box.lineTo(p.x + p.width, p.y)
        box.lineTo(p.x + p.width, p.y + p.height)
        box.lineTo(p.x, p.y + p.height)
        box.lineTo(p.x, p.y)
        g.draw(box)
        g.drawImage(p.im, p.x, p.y, peer)
        val xtoCenter: Int = (p.width - selectBoxWidth) / 2
        val ytoCenter: Int = (p.height - selectBoxWidth) / 2
        for (bee <- p.bees) { // TODO deal with bodyguards, improve centering
          g.drawImage(bee.im, bee.x.toInt + xtoCenter, bee.y + ytoCenter, peer)
        }
        if (p.isAntIn) {
          if (p.ant.isContainer && p.ant.asInstanceOf[BodyguardAnt].ant.isDefined) {
            val underAnt = p.ant.asInstanceOf[BodyguardAnt].ant.get
            g.drawImage(underAnt.im, underAnt.x.toInt + xtoCenter, underAnt.y + ytoCenter, peer)
          }
          g.drawImage(p.ant.im, p.ant.x.toInt + xtoCenter, p.ant.y + ytoCenter, peer)
        }
      }

      /* DRAW USER INTERFACE */
      g.drawString("Food: " + Colony.foodAmount.toString, 10, size.height-10)

      /** Draw a Selecter Box and print cost and armor of the unit, red if selected, black otherwise */
      def drawSelectedBox(x: Int, y: Int, bool: Boolean, cost: Int, armor: Int, icon: ImageIcon) = {
        val currentSelectBox = new geom.GeneralPath
        currentSelectBox.moveTo(x, y)
        currentSelectBox.lineTo(x + selectBoxWidth, y)
        currentSelectBox.lineTo(x + selectBoxWidth,  y + selectBoxHeight)
        currentSelectBox.lineTo(x, y + selectBoxHeight)
        currentSelectBox.lineTo(x, y)
        if (bool) {
          g.setColor(Color.red)
        } else {
          g.setColor(Color.black)
        }
        g.drawString(" Cost: " + cost.toString, x, y + 20)
        g.drawString(" Armor: " + armor.toString, x, y + 40)
        g.draw(currentSelectBox)
        val currentIm = icon.getImage
        g.drawImage(currentIm, x, y + 40, peer)
      }

      //drawSelectedBox(10 + selectBoxWidth * 0, 10, controller.harvesterSelected, 2, 1, harvesterIcon)
      drawSelectedBox(10 + selectBoxWidth * 1, 10, controller.shortThrowerSelected, 3, 1, shortThrowerIcon)
      drawSelectedBox(10 + selectBoxWidth * 2, 10, controller.longThrowerSelected, 3, 1, longThrowerIcon)
      drawSelectedBox(10 + selectBoxWidth * 3, 10, controller.fireSelected, 5, 1, fireIcon)
      drawSelectedBox(10 + selectBoxWidth * 4, 10, controller.scubaSelected, 5, 1, scubaIcon)
      drawSelectedBox(10 + selectBoxWidth * 5, 10, controller.wallSelected, 4, 4, wallIcon)
      drawSelectedBox(10 + selectBoxWidth * 6, 10, controller.ninjaSelected, 6, 1, ninjaIcon)
      drawSelectedBox(10 + selectBoxWidth * 7, 10, controller.hungrySelected, 4, 1, hungryIcon)
      drawSelectedBox(10 + selectBoxWidth * 8, 10, controller.queenSelected, 6, 2, queenIcon)
      drawSelectedBox(10 + selectBoxWidth * 9, 10, controller.bodyGuardSelected, 6, 2, bodyGuardIcon)
      drawSelectedBox(10 + selectBoxWidth * 10, 10, controller.byeSelected, 4, 2, byeIcon)


       // DRAW PROJECTILES
      g.setColor(Color.red)
      for (p <- Projectiles.projectiles) {
        val circle = new geom.Ellipse2D.Double(p.x, p.y + 30, 8, 8)
        g.draw(circle)
      }
    }


    /* USER ACTIONS */
    reactions += {
      case e: MouseReleased =>

        if (10 + selectBoxWidth * 0 <= getPos.x && getPos.x < 10 + selectBoxWidth * 1 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.harvesterClicked()
        } else if (10 + selectBoxWidth * 1 <= getPos.x && getPos.x < 10 + selectBoxWidth * 2 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.shortThrowerClicked()
        } else if (10 + selectBoxWidth * 2 <= getPos.x && getPos.x < 10 + selectBoxWidth * 3 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.longThrowerClicked()
        } else if (10 + selectBoxWidth * 3 <= getPos.x && getPos.x < 10 + selectBoxWidth * 4 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.fireClicked()
        } else if (10 + selectBoxWidth * 4 <= getPos.x && getPos.x < 10 + selectBoxWidth * 5 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.scubaClicked()
        } else if (10 + selectBoxWidth * 5 <= getPos.x && getPos.x < 10 + selectBoxWidth * 6 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.wallClicked()
        } else if (10 + selectBoxWidth * 6 <= getPos.x && getPos.x < 10 + selectBoxWidth * 7 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.ninjaClicked()
        } else if (10 + selectBoxWidth * 7 <= getPos.x && getPos.x < 10 + selectBoxWidth * 8 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.hungryClicked()
        } else if (10 + selectBoxWidth * 8 <= getPos.x && getPos.x < 10 + selectBoxWidth * 9 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.queenClicked()
        } else if (10 + selectBoxWidth * 9 <= getPos.x && getPos.x < 10 + selectBoxWidth * 10 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.bodyGuardClicked()
        } else if (10 + selectBoxWidth * 10 <= getPos.x && getPos.x < 10 + selectBoxWidth * 11 &&
            10 <= getPos.y && getPos.y < 10 + selectBoxHeight) {
          controller.byeClicked()
        } else {
          controller.placeClicked((getPos.x, getPos.y))
        }
      case _: FocusLost => repaint()
    }
  }

  def getUI = ui
  def repaint() { ui.repaint() }
}
