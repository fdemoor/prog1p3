package mvc

import java.awt.{Color, Dimension, Graphics2D, geom}
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
  def isSelected(): Boolean = isSelected_
  def select() = {isSelected_ = true}
  def init() = {isSelected_ = false}
  def isClicked(getX: Int, getY: Int): Boolean = {
    (x <= getX && getX < x + width &&
      y <= getY && getY < y + height)
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
