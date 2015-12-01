package mvc

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._


class Controller(_model: Model) {
  private val model: Model = _model
  private var _view: Option[View] = None
  private var _tTurn: Option[MyTimerTurn] = None
  private var _tFrame: Option[MyTimerFrame] = None

  def view = _view.get
  def tTurn = _tTurn.get
  def tFrame = _tFrame.get

  def freezeCost: Int = model.freezeCost
  def radarCost: Int = model.radarCost
  def doubleCost: Int = model.doubleCost


  /** Add the view as it wasn't created yet when the controller was. Launch the timers. */
  def addView(newView: View) {
    _tTurn = Some(new MyTimerTurn())
    tTurn  // Don't remove, avoid garbage collector
    _tFrame = Some(new MyTimerFrame())
    tFrame  // Don't remove, avoid garbage collector
    _view = Some(newView)
  }


  class MyTimerTurn extends ActionListener {
    /* Configuration */
    val fpsTarget = 50 // Desired amount of frames per second
    var delay = 100000 / fpsTarget

    /* The swing timer */
    val timerTurn = new Timer(delay, this)
    timerTurn.setCoalesce(true) // Please restart by yourself
    timerTurn.start()           // Let's go


    /* Counter to init new wave */
    private var k: Int = 0

    /* react to the timer events */
    def actionPerformed(e: ActionEvent): Unit = {
      model.moveActionsAnts()
      model.moveActionsBees()
      // Starting bee wave after 6 turns, one wave per turn then
      if (k > 5) model.beeWave()
      else k = k + 1
      // Display message
      if (!_view.get.getMsg.isEmpty) _view.get.getMsg.decr()
    }
  }


  class MyTimerFrame extends ActionListener {
    val timerFrame = new Timer(10, this)
    timerFrame.setCoalesce(true)
    timerFrame.start()

    def actionPerformed(e: ActionEvent): Unit = {
      model.move()
      model.moveActionsProjectiles()
      model.removeDeads()
      view.repaint() // Tell Scala that the image should be redrawn
    }
  }


  /** Execute necessary action if a place was clicked */
  def placeClicked(cursorPos: (Int, Int), menu: UIButtonMenu): Unit = {
    for (b <- menu.buttons) {
      if (b.isSelected) {
        b.action(model, cursorPos)
        throw ClickFound()
      }
    }


  }
}
