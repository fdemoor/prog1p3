package mvc

class MVC {
  private val model: Model = new Model
  private val controller: Controller = new Controller(model)
  private val view: View = new View(controller, model.places, model.Colony, model.projectiles)
  controller.addView(view)

  def ui = view.getUI
}
