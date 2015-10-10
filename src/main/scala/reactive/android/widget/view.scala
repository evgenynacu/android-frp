package reactive.android.widget

import android.text.{Editable, TextWatcher}
import android.view.View
import android.view.View.OnClickListener
import android.widget.{AdapterView, TextView}
import reactive.{Observing, EventSource, EventStream, Var}

trait ReactiveViewBase[V <: View] {
  implicit def observing: Observing
  def basis: V

  def watchDistinct[T](s: Var[T], f: T => Unit) = {
    s.distinct.foreach(f)
    s
  }
}

class ReactiveClickableView(val basis: View)(implicit val observing: Observing)
  extends ReactiveViewBase[View] {

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

class ReactiveTextView(val basis: TextView)(implicit val observing: Observing)
  extends ReactiveViewBase[TextView] {

  private def update(value: String): Unit = {
    if (basis.getText.toString != value) {
      basis.setText(value)
    }
  }

  val vText = watchDistinct[String](Var(basis.getText.toString), update)

  basis.addTextChangedListener(new TextWatcher {
    override def beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = {}

    override def onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = {
      vText.update(s.toString)
    }

    override def afterTextChanged(s: Editable) = {}
  })
}