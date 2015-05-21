package reactive.android.content

import android.database.Cursor

class RichCursor(underlying: Cursor) extends Seq[Cursor] {
  def length = underlying.getCount

  def apply(idx: Int) = {
    underlying.moveToPosition(idx)
    underlying
  }

  override def iterator = new Iterator[Cursor] {
    def hasNext = underlying.getCount > 0 && !underlying.isLast

    def next() = {
      underlying.moveToNext()
      underlying
    }
  }
}

object RichCursor {
  implicit def cursorToRich(c: Cursor): RichCursor = new RichCursor(c)
}