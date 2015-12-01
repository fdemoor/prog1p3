package mvc

import java.awt.{Color, Graphics2D}
import javax.swing.ImageIcon


class UIButton(icon: ImageIcon, posX: Int, posY: Int, cost: Int, armor: Int, name: String) {

  def x = posX
  def y = posY

  val width: Int = 66
  val height: Int = 66 + 16

  override def toString = name

  val armorIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/armor.png"))
  val foodIcon: ImageIcon = new ImageIcon(getClass.getResource("/img/food.png"))

  /* Selection methods */
  private var isSelected_ = false
  def isSelected: Boolean = isSelected_
  def select() = {isSelected_ = true}
  def init() = {isSelected_ = false}
  def isClicked(getX: Int, getY: Int): Boolean = {
    x <= getX && getX < x + width && y <= getY && getY < y + height
  }

  /* Action performed when used */
  def action(model: Model, cursorPos: (Int, Int)): Unit = model.tryAddingAnt(cursorPos, this.toString)

  /* Drawing method */
  def paint(g: Graphics2D, peer:java.awt.Component): Unit = {

    if (isSelected) {
      g.setColor(Color.red)
      g.drawRect(x+1, y+1, width-2, height-2)
    } else {
      g.setColor(Color.blue)
    }

    // Food and armor information
    g.drawImage(armorIcon.getImage, x, y, peer)
    g.drawString(" "+armor.toString, x + armorIcon.getIconWidth, y + armorIcon.getIconHeight)
    g.drawImage(foodIcon.getImage, x + + armorIcon.getIconWidth*2, y, peer)
    g.drawString(" "+cost.toString, x + foodIcon.getIconWidth +
      armorIcon.getIconWidth*2, y + foodIcon.getIconHeight)

    g.drawRect(x, y, width, height)
    g.drawImage(icon.getImage, x, y + armorIcon.getIconHeight, peer)
  }

}


class UIButtonRM (icon: ImageIcon, posX: Int, posY: Int)
  extends UIButton(icon, posX, posY, 0, 0, "remove") {

  /* Action performed when used */
  override def action(model: Model, cursorPos: (Int, Int)): Unit = model.tryRemovingAnt(cursorPos)

  /* Drawing method */
  override def paint(g: Graphics2D, peer:java.awt.Component): Unit = {

    if (isSelected) {
      g.setColor(Color.red)
      g.drawRect(x+1, y+1, width-2, height-2)
    } else {
      g.setColor(Color.blue)
    }
    g.drawString(" Remove", x, y + armorIcon.getIconHeight)
    g.drawRect(x, y, width, height)
    g.drawImage(icon.getImage, x, y + armorIcon.getIconHeight, peer)
  }

}


class UIButtonFreeze (icon: ImageIcon, posX: Int, posY: Int, cost: Int)
  extends UIButton(icon, posX, posY, 0, cost, "freeze") {

  override val width: Int = 48
  override val height: Int = 48 + 16

  /* Action performed when used */
  override def action(model: Model, cursorPos: (Int, Int)): Unit = model.tryFreezing(cursorPos)

  /* Drawing method */
  override def paint(g: Graphics2D, peer:java.awt.Component): Unit = {

    if (isSelected) {
      g.setColor(Color.red)
      g.drawRect(x+1, y+1, width-2, height-2)
    } else {
      g.setColor(Color.blue)
    }

    g.drawImage(foodIcon.getImage, x, y, peer)
    g.drawString(" "+cost.toString, x + foodIcon.getIconWidth, y + foodIcon.getIconHeight)

    g.drawRect(x, y, width, height)
    g.drawImage(icon.getImage, x, y + foodIcon.getIconHeight, peer)
  }

}


class UIButtonRadar (icon: ImageIcon, posX: Int, posY: Int, cost: Int)
  extends UIButton(icon, posX, posY, 0, cost, "radar") {

  override val width: Int = 48
  override val height: Int = 48 + 16

  /* Action performed when used */
  override def action(model: Model, cursorPos: (Int, Int)): Unit = model.tryRadar(cursorPos)

  /* Drawing method */
  override def paint(g: Graphics2D, peer:java.awt.Component): Unit = {

    if (isSelected) {
      g.setColor(Color.red)
      g.drawRect(x+1, y+1, width-2, height-2)
    } else {
      g.setColor(Color.blue)
    }

    g.drawImage(foodIcon.getImage, x, y, peer)
    g.drawString(" "+cost.toString, x + foodIcon.getIconWidth, y + foodIcon.getIconHeight)

    g.drawRect(x, y, width, height)
    g.drawImage(icon.getImage, x, y + foodIcon.getIconHeight, peer)
  }

}


class UIButtonDouble (icon: ImageIcon, posX: Int, posY: Int, cost: Int)
  extends UIButton(icon, posX, posY, 0, cost, "double") {

  override val width: Int = 48
  override val height: Int = 48 + 16

  /* Action performed when used */
  override def action(model: Model, cursorPos: (Int, Int)): Unit = model.tryDouble(cursorPos)

  /* Drawing method */
  override def paint(g: Graphics2D, peer:java.awt.Component): Unit = {

    if (isSelected) {
      g.setColor(Color.red)
      g.drawRect(x+1, y+1, width-2, height-2)
    } else {
      g.setColor(Color.blue)
    }

    g.drawImage(foodIcon.getImage, x, y, peer)
    g.drawString(" "+cost.toString, x + foodIcon.getIconWidth, y + foodIcon.getIconHeight)

    g.drawRect(x, y, width, height)
    g.drawImage(icon.getImage, x, y + foodIcon.getIconHeight, peer)
  }

}


class UIButtonMenu (l: List[UIButton]) {

  private var buttons_ = l
  def buttons: List[UIButton] = buttons_

  def add(b: UIButton): Unit ={buttons_ = b::buttons_}

  def init(): Unit = {
      for (b <- buttons) b.init()
  }

  /** Manage the click actions on buttons */
  def mouseAction(getX: Int, getY: Int): Unit = {
    for (b <- this.buttons) {
      if (b.isClicked(getX, getY)) {
        if (b.isSelected) { // Un-Select the button
          b.init()
        } else { // Select the button
          init()
          b.select()
        }
        throw ClickFound() // No need to look the other buttons
      }
    }
  }

  /* Drawing method */
  def paint(g: Graphics2D, peer:java.awt.Component): Unit = {
    for (b <- this.buttons) b.paint(g, peer)
  }

}
