package eu.kanade.t4chiyomi.ui.library

import eu.kanade.t4chiyomi.data.database.models.Manga

sealed class LibrarySelectionEvent {

    class Selected(val manga: Manga) : LibrarySelectionEvent()
    class Unselected(val manga: Manga) : LibrarySelectionEvent()
    class Cleared() : LibrarySelectionEvent()
}