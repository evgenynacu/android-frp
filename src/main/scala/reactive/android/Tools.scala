package reactive.android

import android.view.View

object Tools {
  def getOrCreateTag[V <: View, T](view: V, tag: Int, create: (V) => T): T =
    Option(view.getTag(tag).asInstanceOf[T]).getOrElse {
      val created = create(view)
      view.setTag(tag, created)
      created
    }
}
