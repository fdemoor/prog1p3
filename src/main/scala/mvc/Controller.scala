package mvc

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.Timer


class Controller(_model: Model) {
  private val model: Model = _model
  private var _view: Option[View] = None
  private var _tTurn: Option[MyTimerTurn] = None
  private var _tFrame: Option[MyTimerFrame] = None

  def view = _view.get
  def tTurn = _tTurn.get
  def tFrame = _tFrame.get

  /* User Selecter Interface */
  var harvesterSelected: Boolean = false
  var shortThrowerSelected: Boolean = false
  var longThrowerSelected: Boolean = false
  var fireSelected: Boolean = false
  var scubaSelected: Boolean = false
  var wallSelected: Boolean = false
  var ninjaSelected: Boolean = false
  var hungrySelected: Boolean = false
  var queenSelected: Boolean = false

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

    /* react to the timer events */
    def actionPerformed(e: ActionEvent): Unit = {
      model.moveActions()
      model.removeDeads()
    }
  }
  class MyTimerFrame extends ActionListener {
    val timerFrame = new Timer(10, this)
    timerFrame.setCoalesce(true)
    timerFrame.start()

    def actionPerformed(e: ActionEvent): Unit = {
      model.move()
      view.repaint() // Tell Scala that the image should be redrawn
    }
  }

  def harvesterClicked() { harvesterSelected = !harvesterSelected }
  def shortThrowerClicked() { shortThrowerSelected = !shortThrowerSelected }
  def longThrowerClicked() { longThrowerSelected = !longThrowerSelected }
  def fireClicked() { fireSelected = !fireSelected }
  def scubaClicked() { scubaSelected = !scubaSelected }
  def wallClicked() { wallSelected = !wallSelected }
  def ninjaClicked() { ninjaSelected = !ninjaSelected }
  def hungryClicked() { hungrySelected = !hungrySelected }
  def queenClicked() { queenSelected = !queenSelected }
  
  def placeClicked(cursorPos: (Int, Int)): Unit = {
    if (harvesterSelected) {
      model.tryAddingAnt(cursorPos, "harvester")
      // TODO add the tryAddingAnt for other ants
      // Warning with queen : only one can remain !
    }
  }
}
