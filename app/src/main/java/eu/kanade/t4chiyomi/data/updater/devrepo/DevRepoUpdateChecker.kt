package eu.kanade.t4chiyomi.data.updater.devrepo

import eu.kanade.t4chiyomi.BuildConfig
import eu.kanade.t4chiyomi.data.updater.UpdateChecker
import eu.kanade.t4chiyomi.data.updater.UpdateResult
import eu.kanade.t4chiyomi.network.GET
import eu.kanade.t4chiyomi.network.NetworkHelper
import eu.kanade.t4chiyomi.network.asObservable
import okhttp3.OkHttpClient
import rx.Observable
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class DevRepoUpdateChecker : UpdateChecker() {

    private val client: OkHttpClient by lazy {
        Injekt.get<NetworkHelper>().client.newBuilder()
                .followRedirects(false)
                .build()
    }

    private val versionRegex: Regex by lazy {
        Regex("tachiyomi-r(\\d+).apk")
    }

    override fun checkForUpdate(): Observable<UpdateResult> {
        return client.newCall(GET(DevRepoRelease.LATEST_URL)).asObservable()
                .map { response ->
                    // Get latest repo version number from header in format "Location: tachiyomi-r1512.apk"
                    val latestVersionNumber: String = versionRegex.find(response.header("Location")!!)!!.groupValues[1]

                    if (latestVersionNumber.toInt() > BuildConfig.COMMIT_COUNT.toInt()) {
                        DevRepoUpdateResult.NewUpdate(DevRepoRelease("v$latestVersionNumber"))
                    } else {
                        DevRepoUpdateResult.NoNewUpdate()
                    }
                }
    }

}
