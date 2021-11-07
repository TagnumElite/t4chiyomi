package eu.kanade.t4chiyomi.ui.base.activity

import eu.kanade.t4chiyomi.ui.base.presenter.BasePresenter
import eu.kanade.t4chiyomi.util.LocaleHelper
import nucleus.view.NucleusAppCompatActivity

abstract class BaseRxActivity<P : BasePresenter<*>> : NucleusAppCompatActivity<P>() {

    init {
        @Suppress("LeakingThis")
        LocaleHelper.updateConfiguration(this)
    }

}
