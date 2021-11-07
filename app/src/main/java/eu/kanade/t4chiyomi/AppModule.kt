package eu.kanade.t4chiyomi

import android.app.Application
import com.google.gson.Gson
import eu.kanade.t4chiyomi.data.cache.ChapterCache
import eu.kanade.t4chiyomi.data.cache.CoverCache
import eu.kanade.t4chiyomi.data.database.DatabaseHelper
import eu.kanade.t4chiyomi.data.download.DownloadManager
import eu.kanade.t4chiyomi.data.preference.PreferencesHelper
import eu.kanade.t4chiyomi.data.track.TrackManager
import eu.kanade.t4chiyomi.extension.ExtensionManager
import eu.kanade.t4chiyomi.network.NetworkHelper
import eu.kanade.t4chiyomi.source.SourceManager
import rx.Observable
import rx.schedulers.Schedulers
import uy.kohesive.injekt.api.*

class AppModule(val app: Application) : InjektModule {

    override fun InjektRegistrar.registerInjectables() {

        addSingleton(app)

        addSingletonFactory { PreferencesHelper(app) }

        addSingletonFactory { DatabaseHelper(app) }

        addSingletonFactory { ChapterCache(app) }

        addSingletonFactory { CoverCache(app) }

        addSingletonFactory { NetworkHelper(app) }

        addSingletonFactory { SourceManager(app).also { get<ExtensionManager>().init(it) } }

        addSingletonFactory { ExtensionManager(app) }

        addSingletonFactory { DownloadManager(app) }

        addSingletonFactory { TrackManager(app) }

        addSingletonFactory { Gson() }

        // Asynchronously init expensive components for a faster cold start

        rxAsync { get<PreferencesHelper>() }

        rxAsync { get<NetworkHelper>() }

        rxAsync { get<SourceManager>() }

        rxAsync { get<DatabaseHelper>() }

        rxAsync { get<DownloadManager>() }

    }

    private fun rxAsync(block: () -> Unit) {
        Observable.fromCallable { block() }.subscribeOn(Schedulers.computation()).subscribe()
    }

}
