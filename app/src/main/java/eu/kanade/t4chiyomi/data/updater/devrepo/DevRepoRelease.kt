package eu.kanade.t4chiyomi.data.updater.devrepo

import eu.kanade.t4chiyomi.data.updater.Release

class DevRepoRelease(override val info: String) : Release {

    override val downloadLink: String
        get() = LATEST_URL

    companion object {
        const val LATEST_URL = "https://tachiyomi.kanade.eu/latest"
    }

}
