package reactive.android.preferences

import java.lang

import android.content.SharedPreferences.{Editor, OnSharedPreferenceChangeListener}
import android.content.{Context, SharedPreferences}
import android.preference.PreferenceManager
import android.util.Log
import reactive.{Observing, Var}

import scala.collection.mutable

class ReactivePreferences(implicit ctx: Context) extends Observing {
  private val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
  private val map = mutable.Map[lang.String, FieldType[_]]()

  sealed trait FieldType[T] extends Var[T] {
    protected val fieldName: lang.String
    protected val default: T

    protected def read(): T

    protected def write(editor: SharedPreferences.Editor): Unit

    def refresh(): Unit = {
      update(read())
    }

    ReactivePreferences.this.map(fieldName) = this

    distinct.foreach(value => {
      Log.d("TEST", "updating prefs")
      val editor = prefs.edit()
      write(editor)
      editor.apply()
    })

    Log.d("TEST", s"current $fieldName = $value; read=${read()}")
  }

  class Int(val default: scala.Int, val fieldName: lang.String)
    extends Var[scala.Int](prefs.getInt(fieldName, default)) with FieldType[scala.Int] {

    override def read() = prefs.getInt(fieldName, default)

    override protected def write(editor: Editor) = editor.putInt(fieldName, value)
  }

  class String(val default: lang.String, val fieldName: lang.String)
    extends Var[lang.String](prefs.getString(fieldName, default)) with FieldType[lang.String] {

    override def read() = prefs.getString(fieldName, default)

    override protected def write(editor: Editor) = editor.putString(fieldName, value)
  }

  prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
    override def onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: Predef.String) =
      Option(map(key)).foreach(field => field.refresh())
  })
}
