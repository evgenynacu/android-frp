package reactive.android

import android.view.View
import android.widget.ToggleButton
import reactive.android.Tools.getOrCreateTag
import reactive.android.widget.{TraitCommonReactiveView, TraitReactiveToggleButton}

trait ReactiveDomain {
  val viewTagId: Int

  implicit def viewToReactive(view: View): RichCommonReactiveView =
    getOrCreateTag[View, RichCommonReactiveView](view, viewTagId, v => new RichCommonReactiveView(v))
}

class RichCommonReactiveView(val basis: View) extends TraitCommonReactiveView[View]

class RichReactiveToggleButton(val basis: ToggleButton) extends TraitReactiveToggleButton