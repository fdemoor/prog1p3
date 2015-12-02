/**
  * Third project of the course PROG1
  * Authors: Simon Bihel, Florestan De Moor
  * Institute: ENS Rennes, Computer Science Department
  */

import mvc._

import scala.swing.{MainFrame, SimpleSwingApplication}

object Main extends SimpleSwingApplication {

  private val mvc = new MVC

  def top = new MainFrame {
    title = "Ant vs Bees"
    contents = mvc.ui
  }
}
