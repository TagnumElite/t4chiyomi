package eu.kanade.t4chiyomi.source

import android.support.v7.preference.PreferenceScreen

interface ConfigurableSource : Source {

    fun setupPreferenceScreen(screen: PreferenceScreen)
}