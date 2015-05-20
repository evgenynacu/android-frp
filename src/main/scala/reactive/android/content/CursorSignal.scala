package reactive.android.content

import android.app.LoaderManager
import android.app.LoaderManager.LoaderCallbacks
import android.content.{Context, CursorLoader, Loader}
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import reactive.{Observing, Var, Signal}

class CursorSignal(descriptor: Signal[ContentDescriptor], ctx: Context, id:Int, loaderManager: LoaderManager)
                  (implicit o: Observing)
  extends Signal[Option[Cursor]] {

  private val cursorVar = new Var[Option[Cursor]](None)

  def now = cursorVar.now

  def change = cursorVar.change

  private val callbacks = new LoaderCallbacks[Cursor] {
    def onLoaderReset(loader: Loader[Cursor]) = {}

    def onLoadFinished(loader: Loader[Cursor], data: Cursor) = {
      cursorVar.update(Option(data))
    }

    def onCreateLoader(id: Int, args: Bundle) = {
      val d = descriptor.now
      new CursorLoader(ctx, d.uri, d.projection, d.selection, d.selectionArgs, d.orderBy)
    }
  }

  loaderManager.initLoader(id, null, callbacks)
  for(e <- descriptor.change) {
    loaderManager.restartLoader(id, null, callbacks)
  }
}

case class ContentDescriptor(uri: Uri,
                             projection: Array[String],
                             orderBy: String,
                             selection: String = null,
                             selectionArgs: Array[String] = null)
