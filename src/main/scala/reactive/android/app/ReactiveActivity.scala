package reactive.android.app

import android.app.Activity
import android.os.Bundle

class ReactiveActivity extends Activity with ReactiveContext {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    _eCreate.fire(savedInstanceState)
  }

  override def onDestroy(): Unit = {
    super.onDestroy()
    _eDestroy.fire()
  }

  override def onResume(): Unit = {
    super.onResume()
    _eResume.fire()
  }

  override def onSaveInstanceState(outState: Bundle): Unit = {
    super.onSaveInstanceState(outState)
    _eSaveInstanceState.fire(outState)
  }

  override def onPause(): Unit = {
    super.onPause()
    _ePause.fire()
  }
}
