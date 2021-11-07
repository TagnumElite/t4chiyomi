package eu.kanade.t4chiyomi.ui.catalogue.browse

import eu.kanade.t4chiyomi.source.CatalogueSource
import eu.kanade.t4chiyomi.source.model.FilterList
import eu.kanade.t4chiyomi.source.model.MangasPage
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class CataloguePager(val source: CatalogueSource, val query: String, val filters: FilterList) : Pager() {

    override fun requestNext(): Observable<MangasPage> {
        val page = currentPage

        val observable = if (query.isBlank() && filters.isEmpty())
            source.fetchPopularManga(page)
        else
            source.fetchSearchManga(page, query, filters)

        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.mangas.isNotEmpty()) {
                        onPageReceived(it)
                    } else {
                        throw NoResultsException()
                    }
                }
    }

}