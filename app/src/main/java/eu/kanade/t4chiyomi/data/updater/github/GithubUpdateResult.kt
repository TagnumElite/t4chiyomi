package eu.kanade.t4chiyomi.data.updater.github

import eu.kanade.t4chiyomi.data.updater.UpdateResult

sealed class GithubUpdateResult : UpdateResult() {

    class NewUpdate(release: GithubRelease): UpdateResult.NewUpdate<GithubRelease>(release)
    class NoNewUpdate : UpdateResult.NoNewUpdate()

}
