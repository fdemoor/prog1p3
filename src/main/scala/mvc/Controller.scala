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
  var bodyGuardSelected: Boolean = false
  var byeSelected: Boolean = false
  
  
  def initSelecter() = {
    harvesterSelected = false
    shortThrowerSelected = false
    longThrowerSelected = false
    fireSelected = false
    scubaSelected = false
    wallSelected = false
    ninjaSelected = false
    hungrySelected = false
    queenSelected = false
    bodyGuardSelected = false
    byeSelected = false
  }

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
      model.moveActionsAnts()
      // Display attacks ? -> when projectile created, you have to add it to _projectiles in model
      model.removeDeads()
      model.moveActionsBees()
      // Display attacks ?
      model.moveActionsProjectiles()
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

  def harvesterClicked() {
    if (!harvesterSelected) initSelecter()
      harvesterSelected = !harvesterSelected
  }
  def shortThrowerClicked() {
    if (!shortThrowerSelected) initSelecter()
      shortThrowerSelected = !shortThrowerSelected
  }
  def longThrowerClicked() {
    if (!longThrowerSelected) initSelecter()
      longThrowerSelected = !longThrowerSelected
  }
  def fireClicked() {
    if (!fireSelected) initSelecter()
      fireSelected = !fireSelected
  }
  def scubaClicked() {
    if (!scubaSelected) initSelecter()
      scubaSelected = !scubaSelected
  }
  def wallClicked() {
    if (!wallSelected) initSelecter()
      wallSelected = !wallSelected
  }
  def ninjaClicked() {
    if (!ninjaSelected) initSelecter()
      ninjaSelected = !ninjaSelected
  }
  def hungryClicked() {
    if (!hungrySelected) initSelecter()
      hungrySelected = !hungrySelected
  }
  def queenClicked() {
    if (!queenSelected) initSelecter()
      queenSelected = !queenSelected
  }
  def bodyGuardClicked() {
    if (!bodyGuardSelected) initSelecter()
      bodyGuardSelected = !bodyGuardSelected
  }
  def byeClicked() {
    if (!byeSelected) initSelecter() 
      byeSelected = !byeSelected
  }

  def placeClicked(cursorPos: (Int, Int)): Unit = {
    if (harvesterSelected) {
      model.tryAddingAnt(cursorPos, "harvester")
    } else if (shortThrowerSelected) {
      model.tryAddingAnt(cursorPos, "shortThrower")
    } else if (longThrowerSelected) {
      model.tryAddingAnt(cursorPos, "longThrower")
    } else if (fireSelected) {
      model.tryAddingAnt(cursorPos, "fire")
    } else if (scubaSelected) {
      model.tryAddingAnt(cursorPos, "scuba")
    } else if (wallSelected) {
      model.tryAddingAnt(cursorPos, "wall")
    } else if (ninjaSelected) {
      model.tryAddingAnt(cursorPos, "ninja")
    } else if (wallSelected) {
      model.tryAddingAnt(cursorPos, "hungry")
    } else if (queenSelected) {
      model.tryAddingAnt(cursorPos, "queen")
    } else if (bodyGuardSelected) {
      model.tryAddingAnt(cursorPos, "bodyGuard")
    } else if (byeSelected) {
      model.tryRemovingAnt(cursorPos)
    }
  }
  
}
