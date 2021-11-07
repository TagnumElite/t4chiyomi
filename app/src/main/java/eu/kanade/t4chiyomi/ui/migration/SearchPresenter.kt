package eu.kanade.t4chiyomi.ui.migration

import eu.kanade.t4chiyomi.data.database.models.Manga
import eu.kanade.t4chiyomi.source.CatalogueSource
import eu.kanade.t4chiyomi.source.model.SManga
import eu.kanade.t4chiyomi.ui.catalogue.global_search.CatalogueSearchCardItem
import eu.kanade.t4chiyomi.ui.catalogue.global_search.CatalogueSearchItem
import eu.kanade.t4chiyomi.ui.catalogue.global_search.CatalogueSearchPresenter

class SearchPresenter(
        initialQuery: String? = "",
        private val manga: Manga
) : CatalogueSearchPresenter(initialQuery) {

    override fun getEnabledSources(): List<CatalogueSource> {
        // Put the source of the selected manga at the top
        return super.getEnabledSources()
                .sortedByDescending { it.id == manga.source }
    }

    override fun createCatalogueSearchItem(source: CatalogueSource, results: List<CatalogueSearchCardItem>?): CatalogueSearchItem {
        //Set the catalogue search item as highlighted if the source matches that of the selected manga
        return CatalogueSearchItem(source, results, source.id == manga.source)
    }

    override fun networkToLocalManga(sManga: SManga, sourceId: Long): Manga {
        val localManga = super.networkToLocalManga(sManga, sourceId)
        // For migration, displayed title should always match source rather than local DB
        localManga.title = sManga.title
        return localManga
    }
}
