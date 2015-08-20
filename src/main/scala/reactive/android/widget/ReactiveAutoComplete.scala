package reactive.android.widget

import android.content.Context
import android.text.{Editable, TextWatcher}
import android.util.Log
import android.widget.Filter.FilterResults
import android.widget.{AutoCompleteTextView, Filter, Filterable, ArrayAdapter}
import reactive.{Observing, Var}

class ReactiveAutoComplete[T <: AnyRef](vValue: Var[Option[T]])
                                       (autocomplete: String => Seq[T])
                                       (listItem: Int, textViewResourceId: Int)
                                       (implicit ctx: Context)
  extends AutoCompleteTextView(ctx) with Observing {

  private lazy val _adapter = new Adapter()

  for (value <- vValue.distinct) {
    Log.d("TEST", "vValue changed")
    val stringValue = value.map(_.toString).getOrElse("")
    if (getText.toString != stringValue) {
      setAdapter(null)
      _adapter.resultList = value.toList
      setText(stringValue)
      setAdapter(_adapter)
    }
  }

  addTextChangedListener(new TextWatcher {
    override def afterTextChanged(s: Editable) {}

    override def onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      vValue.update(_adapter.resultList.find(v => v.toString.equals(s.toString)))
    }

    override def beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
  })

  class Adapter extends ArrayAdapter[T](ctx, listItem, textViewResourceId) with Filterable {
    private[android] var resultList: Seq[T] = List()

    override def getCount = resultList.size

    override def getItem(position: Int): T = resultList(position)

    override def getFilter = new Filter() {
      override def publishResults(constraint: CharSequence, results: FilterResults) =
        if (results != null && results.count > 0) {
          notifyDataSetChanged()
        } else {
          notifyDataSetInvalidated()
        }

      override def performFiltering(constraint: CharSequence) = {
        Log.d("TEST", "performFiltering")
        val res = new FilterResults
        if (constraint != null) {
          resultList = autocomplete(constraint.toString)
          Log.d("TEST", "autocompleted")
          res.count = resultList.size
          res.values = resultList
        }
        res
      }
    }
  }

  setAdapter(_adapter)
}