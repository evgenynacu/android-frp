package reactive.android

import android.view.View
import android.widget.TextView
import reactive.Observing
import reactive.android.Tools.getOrCreateTag
import reactive.android.widget.{ReactiveClickableView, ReactiveTextView}

trait ReactiveDomain {
  val clickableViewTagId: Int
  val textViewTagId: Int

  implicit def clickableView(view: View)(implicit observing: Observing): ReactiveClickableView =
    getOrCreateTag[View, ReactiveClickableView](view, clickableViewTagId, v => new ReactiveClickableView(v))

  implicit def textViewToReactive(view: TextView)(implicit observing: Observing): ReactiveTextView =
    getOrCreateTag[TextView, ReactiveTextView](view, textViewTagId, v => new ReactiveTextView(v))
}

