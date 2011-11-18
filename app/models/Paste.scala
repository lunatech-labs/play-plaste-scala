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