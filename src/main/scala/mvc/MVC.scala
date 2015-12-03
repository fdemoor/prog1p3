/**
  * Contains elements of the Model-View-Controller architecture. Makes the link between the UI and the game.
  */
package mvc

/** Creates and stores the elements of the mvc to have an operational game. */
class MVC {
  private val model: Model = new Model
  private val controller: Controller = new Controller(model)
  private val view: View = new View(controller, model.gridGame, model.Colony)
  controller.addView(view)

  def ui = view.getUI
}
