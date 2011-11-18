package controllers

import play.api.data._
import play.api.mvc._
import models.Paste
import play.api.Logger
import java.util.Date
import anorm.NotAssigned

object Application extends Controller {

  /** Form for a new paste. */
  val form = Form(
    of(Paste.apply _)(
      "id" -> ignored(NotAssigned),
      "title" -> requiredText,
      "code" -> requiredText,
      "pastedAt" -> ignored(new Date)
    )
  )

  /** Renders a page for submitting a new paste. */
  def index = Action {
    Ok(views.html.index(form))
  }

  /** Validates and saves a new paste. */
  def post = Action {
    implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.index(formWithErrors)),
      paste => {
        Paste.insert(paste)
        Ok(paste.title + " (" + paste.pastedAt + ")\n" +  paste.code)
      }
    )
  }

  /** Render a page for a single post. */
  def show(id:Long) = Action {
    Ok(views.html.show(Paste.find(id)))
  }
}