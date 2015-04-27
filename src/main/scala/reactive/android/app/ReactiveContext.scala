package reactive.android.app

import android.os.Bundle
import reactive.{EventSource, EventStream}

trait ReactiveContext {
  protected lazy val _eCreate = new EventSource[Bundle]
  protected lazy val _eDestroy = new EventSource[Unit]
  protected lazy val _ePause = new EventSource[Unit]
  protected lazy val _eResume = new EventSource[Unit]
  protected lazy val _eSaveInstanceState = new EventSource[Bundle]

  lazy val eCreate: EventStream[Bundle] = _eCreate
  lazy val eDestroy: EventStream[Unit] = _eDestroy
  lazy val ePause: EventStream[Unit] = _ePause
  lazy val eResume: EventStream[Unit] = _eResume
  lazy val eSaveInstanceState: EventStream[Bundle] = _eSaveInstanceState
}
