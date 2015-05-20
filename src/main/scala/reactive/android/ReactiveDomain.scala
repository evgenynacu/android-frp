package reactive.android

import android.view.View
import reactive.android.Tools.getOrCreateTag
import reactive.android.widget.TraitCommonReactiveView

trait ReactiveDomain {
  val viewTagId: Int

  implicit def viewToReactive(view: View): RichCommonReactiveView =
    getOrCreateTag[View, RichCommonReactiveView](view, viewTagId, v => new RichCommonReactiveView(v))
}

class RichCommonReactiveView(val basis: View) extends TraitCommonReactiveView[View]

