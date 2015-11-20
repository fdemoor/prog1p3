package mvc

class MVC {
  val model: Model = new Model
  val controller: Controller = new Controller(model)
  val view: View = new View(controller, model.places, model.Colony)
  controller.addView(view)

  def ui = view.getUI
}
