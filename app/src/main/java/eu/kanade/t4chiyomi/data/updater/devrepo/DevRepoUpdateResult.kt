package eu.kanade.t4chiyomi.data.updater.devrepo

import eu.kanade.t4chiyomi.data.updater.UpdateResult

sealed class DevRepoUpdateResult : UpdateResult() {

    class NewUpdate(release: DevRepoRelease): UpdateResult.NewUpdate<DevRepoRelease>(release)
    class NoNewUpdate: UpdateResult.NoNewUpdate()

}
