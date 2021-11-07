package eu.kanade.t4chiyomi.ui.library

import eu.kanade.t4chiyomi.data.database.models.Category

class LibraryMangaEvent(val mangas: Map<Int, List<LibraryItem>>) {

    fun getMangaForCategory(category: Category): List<LibraryItem>? {
        return mangas[category.id]
    }
}
