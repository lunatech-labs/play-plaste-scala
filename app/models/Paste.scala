package models

import java.util.Date
import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

/** A content snippet, typically a code sample, as text or a binary attachment. */
case class Paste(
  id: Pk[Long],
  title: String,
  code: String,
  pastedAt: Date
)

/** Data access helpers. */
object Paste {

  /** Parses a paste from a ResultSet. */
  val simple = {
    get[Pk[Long]]("paste.id") ~/
    get[String]("paste.title") ~/
    get[String]("paste.code") ~/
    get[Date]("paste.pastedAt") ^^ {
      case id~title~code~pastedAt => Paste(id, title, code, pastedAt)
    }
  }
  
  /** Find a paste by ID. */
  def find(id: Long): Paste = {
    DB.withConnection { implicit connection =>
      SQL("select * from paste where id = {id}").on('id -> id).as(Paste.simple)
    }
  }

  /** Inserts a new paste. */
  def insert(paste: Paste) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into paste values (
            (select next value for paste_seq),
            {title}, {code}, {pastedAt}
          )
        """
      ).on(
        'title -> paste.title,
        'code -> paste.code,
        'pastedAt -> paste.pastedAt
      ).executeUpdate()
    }
  }
}