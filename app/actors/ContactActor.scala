package actors

import akka.actor.{Actor, ActorLogging}
import Actor._
import akka.event.Logging

import play.api.libs.functional.syntax._
import play.api.libs.iteratee.{ Concurrent, Enumerator, Iteratee }
import play.api.libs.json._
import play.api.Play.current

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

import models._

case class WebSocketSubscribe()
case class WebSocketUnsubscribe(enumerator: Enumerator[_])
case class Delete(id: Long)

class ContactActor extends Actor with ActorLogging {
  var subscriberCount = 0
  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  implicit val contactWrites = Json.writes[Contact]
  implicit val contactReads = (
    (__ \ "station_id").read[String] and
    (__ \ "callsign").read[String] and
    (__ \ "exchange").read[String] and
    (__ \ "band").read[String] and
    (__ \ "mode").read[String]
  ) tupled

  def receive = {
    case WebSocketSubscribe() => {
      val iteratee = Iteratee.foreach[JsValue] { input =>
        input.validate[(String, String, String, String, String)].map {
          case (stationID, callsign, exchange, band, mode) => {
            self ! Contact(None, callsign, exchange, band, mode, stationID.toInt, None)
          }
        }.recoverTotal {
          e => println(JsError.toFlatJson(e))
        }
      }.mapDone(_ => self ! WebSocketUnsubscribe(enumerator))

      subscriberCount += 1
      log.info(s"Someone subscribed - ${subscriberCount} subscribers.")
      sender ! (iteratee, enumerator)
    }
    case WebSocketUnsubscribe(subscriber) => {
      subscriberCount -= 1
      log.info(s"Someone unsubscribed - ${subscriberCount} subscribers.")
    }
    case e: Contact => {
      try {
        val id = e.save()
        val res = Contact.findByID(id.getOrElse(0))
        channel.push(Json.toJson(res))
        log.info(res.toString)
      } catch {
        case e: MySQLIntegrityConstraintViolationException => {
          channel.push(Json.toJson(Map("error" -> "DUPE!!!")))
        }
      }
    }
    case Delete(id) => {
      val deleteJson = Map("delete" -> id.toString)
      channel.push(Json.toJson(deleteJson))
    }
    case e => log.info(e.toString)
  }
}
