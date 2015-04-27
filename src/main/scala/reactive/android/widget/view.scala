package reactive.android.widget

import android.view.View

trait TraitReactiveView[V <: View] {
  def basis: V
}

trait TraitCommonReactiveView[V <: View]
  extends TraitReactiveView[V]
  with ClickableReactiveView[V] {

}
