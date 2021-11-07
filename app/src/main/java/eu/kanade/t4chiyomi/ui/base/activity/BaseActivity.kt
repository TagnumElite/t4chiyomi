package eu.kanade.t4chiyomi.ui.base.activity

import android.support.v7.app.AppCompatActivity
import eu.kanade.t4chiyomi.util.LocaleHelper

abstract class BaseActivity : AppCompatActivity() {

    init {
        @Suppress("LeakingThis")
        LocaleHelper.updateConfiguration(this)
    }

}
