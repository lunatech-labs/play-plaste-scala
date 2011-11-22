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

  /** Fetch all pastes. */
  def list(): Seq[Paste] = {
    DB.withConnection { implicit connection =>
      SQL("select * from paste order by pastedAt desc").as(Paste.simple *)
    }
  }

  /** Inserts a new paste and returns its generated ID. */
  def create(paste: Paste): Option[Long] = {
    DB.withConnection { implicit connection =>
      val query = SQL(
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
      )

      // Extract the generated ID.
      // TODO: find a nicer way to do this, using a SQL parser or Magic.
      val (statement, ok) = query.execute1(getGeneratedKeys = true)
      val results = statement.getGeneratedKeys()
      if (results.next()) Some(results.getLong(1)) else None
    }
  }
}