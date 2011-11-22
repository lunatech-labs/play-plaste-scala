package controllers

import play.api.data._
import play.api.mvc._
import play.api.mvc.Results.Redirect
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

  /** Renders a page that shows a history list of pastes. */
  def history = Action {
    implicit request =>
    Ok(views.html.history(Paste.list()))
  }

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
        val newPaste = Paste.create(paste)
        newPaste match {
          case None => Ok("No ID returned")
          case Some(id) => Redirect(routes.Application.show(id))
        }
      }
    )
  }

  /** Render a page for a single post. */
  def show(id:Long) = Action {
    Ok(views.html.show(Paste.find(id)))
  }
}