package eu.kanade.t4chiyomi.data.updater

import eu.kanade.t4chiyomi.BuildConfig
import eu.kanade.t4chiyomi.data.updater.devrepo.DevRepoUpdateChecker
import eu.kanade.t4chiyomi.data.updater.github.GithubUpdateChecker
import rx.Observable

abstract class UpdateChecker {

    companion object {
        fun getUpdateChecker(): UpdateChecker {
            return if (BuildConfig.DEBUG) {
                DevRepoUpdateChecker()
            } else {
                GithubUpdateChecker()
            }
        }
    }

    /**
     * Returns observable containing release information
     */
    abstract fun checkForUpdate(): Observable<UpdateResult>

}
