package controllers

import models._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.libs.json._
import play.api.Play.current

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt

import actors._

object Application extends Controller {
  implicit val timeout = Timeout(1.second)
  val listener = Akka.system.actorOf(Props[ContactActor], name = "contactactor")

  def contacts = Contact.getAll

  def index = Action { implicit request =>
    Ok(views.html.index(contacts))
  }

  def delete(id: Long) = Action { implicit request =>
    Contact.delete(id)
    listener ! Delete(id)
    Ok("OK")
  }

  def websocket = WebSocket.async[JsValue] { request =>
    val future = listener ? WebSocketSubscribe()
    future.mapTo[(Iteratee[JsValue, _], Enumerator[JsValue])]
  }
}
