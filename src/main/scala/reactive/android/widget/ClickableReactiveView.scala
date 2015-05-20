package reactive.android.widget

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import reactive.{EventSource, EventStream}

trait ClickableReactiveView[V <: View] extends TraitReactiveView[V] {

  private val _eClicks = new EventSource[View]

  lazy val eClicks: EventStream[View] = {
    basis match {
      case av: AdapterView[_] =>
      case _ => basis.setOnClickListener(new OnClickListener {
        def onClick(v: View) = _eClicks.fire(basis)
      })
    }
    _eClicks
  }
}

