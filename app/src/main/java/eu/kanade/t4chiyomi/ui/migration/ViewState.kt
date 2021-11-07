package eu.kanade.t4chiyomi.ui.migration

import eu.kanade.t4chiyomi.source.Source

data class ViewState(
        val selectedSource: Source? = null,
        val mangaForSource: List<MangaItem> = emptyList(),
        val sourcesWithManga: List<SourceItem> = emptyList(),
        val isReplacingManga: Boolean = false
)