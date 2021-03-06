package mvc

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

/**
  * Control the pace of the game.
  */
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

  /** Timer activated (=actions to do) on each turn. */
  class MyTimerTurn extends ActionListener {
    /* Configuration */
    val fpsTarget = 50 // Desired amount of frames per second
    var delay = 100000 / fpsTarget

    /* The swing timer */
    val timerTurn = new Timer(delay, this)
    timerTurn.setCoalesce(true) // Please restart by yourself
    timerTurn.start()           // Let's go


    /* Counter to init first wave */
    private var waveNumber: Int = 0
    /* Counter to increase wave difficulty */
    private var _waveDifficulty: Int = 0
    private var _beeLvl: Int = 1

    def waveDifficulty: Int = _waveDifficulty
    def beeLvl: Int = _beeLvl

    /* react to the timer events */
    def actionPerformed(e: ActionEvent): Unit = {
      model.moveActionsAnts()
      model.moveActionsBees()
      // Starting bee wave after 6 turns, one wave per turn then
      // Increase wave difficulty after 10 turns
      if (waveNumber > 5) {
        model.beeWave(this.beeLvl, this.waveDifficulty)
        _waveDifficulty = (_waveDifficulty + 1)%20
        if (this.waveDifficulty == 0) _beeLvl = _beeLvl+1
      } else waveNumber = waveNumber + 1
      // Display message
      if (!_view.get.getMsg.isEmpty) _view.get.getMsg.decr()
    }
  }

  /** Timer activated (=actions to do) on each frame. */
  class MyTimerFrame extends ActionListener {
    val timerFrame = new Timer(10, this)
    timerFrame.setCoalesce(true)
    timerFrame.start()

    def actionPerformed(e: ActionEvent): Unit = {
      model.move()
      model.moveActionsProjectiles()
      model.removeDeads()
      view.repaint() // Tell Scala that the image should be redrawn
      if (model.isEnded) {
        view.getMsg.setMsg("Game Over.")
        view.repaint()
        _tTurn.get.timerTurn.stop()
        timerFrame.stop()
      }
    }
  }


  /** Execute necessary action if a place was clicked */
  def placeClicked(cursorPos: (Int, Int), menu: UIButtons): Unit = {
    for (b <- menu.buttons) {
      if (b.isSelected) {
        b.action(model, cursorPos)
        throw ClickFound()
      }
    }
  }
}
