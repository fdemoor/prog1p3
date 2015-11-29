package mvc

import colony.Colony, places._, projectiles.Projectiles
import java.awt.{Color, Dimension, Graphics2D, geom}
import javax.swing.ImageIcon
import insects.BodyguardAnt

import scala.swing.event._
import scala.swing.Panel


/** Paint the place and all that is inside */


class View(_controller: Controller, grid: Grid, _Colony: Colony) {

  private val controller: Controller = _controller
  //private val places: List[Place] = placesList
  private val Colony: Colony = _Colony
  
  
  
  def paintGrid(gridGame: Grid, g: Graphics2D, peer:java.awt.Component): Unit = {
    
    for (p <- gridGame.places) {
      g.setColor(Color.black)
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

    // MENU
    val menu = new UIButtonMenu(Nil: List[UIButton])

    val harvesterIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_harvester.png"))
    val harvesterButton = new UIButton(harvesterIcon, 10, 10, 2, 1)
    menu.add(harvesterButton)

    val shortThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_shortthrower.png"))
    val shortThrowerButton = new UIButton(shortThrowerIcon, 10 + menu.buttons.head.width, 10, 3 ,1)
    menu.add(shortThrowerButton)
    
    val longThrowerIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_longthrower.png"))
    val longThrowerButton = new UIButton(longThrowerIcon, 10 + menu.buttons.head.width*2, 10, 3 ,1)
    menu.add(longThrowerButton)
    
    val fireIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_fire.png"))
    val fireButton = new UIButton(fireIcon, 10 + menu.buttons.head.width*3, 10, 5 ,1)
    menu.add(fireButton)
    
    val scubaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_scuba.png"))
    val scubaButton = new UIButton(scubaIcon, 10 + menu.buttons.head.width*4, 10, 5 ,1)
    menu.add(scubaButton)
    
    val wallIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_wall.png"))
    val wallButton = new UIButton(wallIcon, 10 + menu.buttons.head.width*5, 10, 4 ,4)
    menu.add(wallButton)
    
    val ninjaIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_ninja.png"))
    val ninjaButton = new UIButton(ninjaIcon, 10 + menu.buttons.head.width*6, 10, 6 ,1)
    menu.add(ninjaButton)
    
    val hungryIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_hungry.png"))
    val hungryButton = new UIButton(hungryIcon, 10 + menu.buttons.head.width*7, 10, 4 ,1)
    menu.add(hungryButton)
    
    val queenIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_queen.png"))
    val queenButton = new UIButton(queenIcon, 10 + menu.buttons.head.width*8, 10, 6 ,2)
    menu.add(queenButton)
    
    val bodyGuardIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/ant_weeds.png"))
    val bodyGuardButton = new UIButton(bodyGuardIcon, 10 + menu.buttons.head.width*9, 10, 6 ,2)
    menu.add(bodyGuardButton)
    
    val byeIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/remover.png"))
    val byeButton = new UIButton(byeIcon, 10 + menu.buttons.head.width*10, 10, 4 ,2)
    menu.add(byeButton)
    
    val selectBoxWidth: Int = 66
    val selectBoxHeight: Int = 66 + 40


    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      //g.setColor(new Color(100, 100, 100))
      val pos = getPos
      if (pos != null) g.drawString("x: "+pos.x+" y: "+pos.y, size.width-85, 15)


      menu.paint(g, peer)

      /*// DRAW PLACES AND INSECTS //
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
        for (bee <- p.bees) {
          g.drawImage(bee.im, bee.x.toInt + xtoCenter, bee.y + ytoCenter, peer)
        }
        if (p.isAntIn) {
          if (p.ant.isContainer && p.ant.asInstanceOf[BodyguardAnt].ant.isDefined) {
            val underAnt = p.ant.asInstanceOf[BodyguardAnt].ant.get
            g.drawImage(underAnt.im, underAnt.x.toInt + xtoCenter, underAnt.y + ytoCenter, peer)
          }
          g.drawImage(p.ant.im, p.ant.x.toInt + xtoCenter, p.ant.y + ytoCenter, peer)
        }
      }*/
      
      paintGrid(grid, g, peer)

      /* BOTTOM INFO BAR */
      val foodIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/food.png"))
      g.drawImage(foodIcon.getImage, 10, size.height-26, peer)
      g.drawString(" " + Colony.foodAmount.toString, 10+16, size.height-10)
      
      val scoreIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/score.png"))
      g.drawImage(scoreIcon.getImage, 10+32+40, size.height-26, peer)
      g.drawString(" " + Colony.scoreAmount.toString, 10+48+40, size.height-10)

      /** Draw a Selecter Box and print cost and armor of the unit, red if selected, black otherwise */
      /*def drawSelectedBox(x: Int, y: Int, bool: Boolean, cost: Int, armor: Int, icon: ImageIcon) = {
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
      }*/

      /*drawSelectedBox(10 + selectBoxWidth * 0, 10, controller.harvesterSelected, 2, 1, harvesterIcon)
      drawSelectedBox(10 + selectBoxWidth * 1, 10, controller.shortThrowerSelected, 3, 1, shortThrowerIcon)
      drawSelectedBox(10 + selectBoxWidth * 2, 10, controller.longThrowerSelected, 3, 1, longThrowerIcon)
      drawSelectedBox(10 + selectBoxWidth * 3, 10, controller.fireSelected, 5, 1, fireIcon)
      drawSelectedBox(10 + selectBoxWidth * 4, 10, controller.scubaSelected, 5, 1, scubaIcon)
      drawSelectedBox(10 + selectBoxWidth * 5, 10, controller.wallSelected, 4, 4, wallIcon)
      drawSelectedBox(10 + selectBoxWidth * 6, 10, controller.ninjaSelected, 6, 1, ninjaIcon)
      drawSelectedBox(10 + selectBoxWidth * 7, 10, controller.hungrySelected, 4, 1, hungryIcon)
      drawSelectedBox(10 + selectBoxWidth * 8, 10, controller.queenSelected, 6, 2, queenIcon)
      drawSelectedBox(10 + selectBoxWidth * 9, 10, controller.bodyGuardSelected, 6, 2, bodyGuardIcon)
      drawSelectedBox(10 + selectBoxWidth * 10, 10, controller.byeSelected, 4, 2, byeIcon)*/


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
        menu.mouseAction(getPos.x, getPos.y)

       /*if (10 + selectBoxWidth * 0 <= getPos.x && getPos.x < 10 + selectBoxWidth * 1 &&
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
      case _: FocusLost => repaint()*/
    }
  }

  def getUI = ui
  def repaint() { ui.repaint() }
}
