package eu.kanade.t4chiyomi.ui.catalogue.latest

import eu.kanade.t4chiyomi.source.model.FilterList
import eu.kanade.t4chiyomi.ui.catalogue.browse.BrowseCataloguePresenter
import eu.kanade.t4chiyomi.ui.catalogue.browse.Pager

/**
 * Presenter of [LatestUpdatesController]. Inherit BrowseCataloguePresenter.
 */
class LatestUpdatesPresenter(sourceId: Long) : BrowseCataloguePresenter(sourceId) {

    override fun createPager(query: String, filters: FilterList): Pager {
        return LatestUpdatesPager(source)
    }

}