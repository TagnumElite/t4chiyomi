package eu.kanade.t4chiyomi.ui.catalogue.latest

import eu.kanade.t4chiyomi.source.CatalogueSource
import eu.kanade.t4chiyomi.source.model.MangasPage
import eu.kanade.t4chiyomi.ui.catalogue.browse.Pager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * LatestUpdatesPager inherited from the general Pager.
 */
class LatestUpdatesPager(val source: CatalogueSource): Pager() {

    override fun requestNext(): Observable<MangasPage> {
        return source.fetchLatestUpdates(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { onPageReceived(it) }
    }

}
