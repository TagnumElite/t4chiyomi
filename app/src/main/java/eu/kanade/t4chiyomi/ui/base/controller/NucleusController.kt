package eu.kanade.t4chiyomi.ui.base.controller

import android.os.Bundle
import eu.kanade.t4chiyomi.ui.base.presenter.NucleusConductorDelegate
import eu.kanade.t4chiyomi.ui.base.presenter.NucleusConductorLifecycleListener
import nucleus.factory.PresenterFactory
import nucleus.presenter.Presenter

@Suppress("LeakingThis")
abstract class NucleusController<P : Presenter<*>>(val bundle: Bundle? = null) : RxController(bundle),
        PresenterFactory<P> {

    private val delegate = NucleusConductorDelegate(this)

    val presenter: P
        get() = delegate.presenter

    init {
        addLifecycleListener(NucleusConductorLifecycleListener(delegate))
    }
}
