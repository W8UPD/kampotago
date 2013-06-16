package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

import java.util.Date
import org.joda.time.{DateTime, Period}
import org.joda.time.format.{DateTimeFormat, PeriodFormat}

case class Contact(id: Option[Long], callsign: String, exchange: String, band: String, mode: String, stationID: Int, contactedAt: Option[DateTime], formattedTime: Option[String] = None) {
  def save() = DB.withConnection { implicit c =>
    SQL("""INSERT INTO contacts(callsign, exchange, band, mode, active, station_id)
        VALUES({callsign}, {exchange}, {band}, {mode}, 1, {stationID})""").on(
          'callsign -> callsign.toUpperCase,
          'exchange -> exchange.toUpperCase,
          'band -> band,
          'mode -> mode,
          'stationID -> stationID
    ).executeInsert()
  }
}

object Contact {
  val simple = {
    get[Option[Long]]("id") ~
    get[String]("callsign") ~
    get[String]("exchange") ~
    get[String]("band") ~
    get[String]("mode") ~
    get[Int]("station_id") ~
    get[Option[Date]]("contacted_at") map {
      case id~callsign~exchange~band~mode~stationID~contactedAt => Contact(
        id,
        callsign,
        exchange,
        band,
        mode,
        stationID,
        Some(new DateTime(contactedAt.get)),
        Some(new DateTime(contactedAt.get)).map(ts => DateTimeFormat.fullDateTime.print(ts)))
    }
  }

  def findByCallsign(callsign: String): Seq[Contact] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM contacts WHERE callsign={callsign} AND active=1").on(
      'callsign -> callsign
    ).as(Contact.simple *)
  }

  def findByID(id: Long): Contact = DB.withConnection { implicit c =>
    SQL("SELECT * FROM contacts WHERE id={id} AND active=1").on(
      'id -> id
    ).as(Contact.simple.single)
  }

  def getAll(): Seq[Contact] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM contacts WHERE active=1 ORDER BY id DESC").as(Contact.simple *)
  }

  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("UPDATE contacts SET active=0 WHERE id={id}").on(
      'id -> id
    ).executeUpdate()
  }
}
