package eu.kanade.t4chiyomi.data.database.resolvers

import android.database.Cursor
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver
import eu.kanade.t4chiyomi.data.database.mappers.BaseMangaGetResolver
import eu.kanade.t4chiyomi.data.database.models.LibraryManga
import eu.kanade.t4chiyomi.data.database.tables.MangaTable

class LibraryMangaGetResolver : DefaultGetResolver<LibraryManga>(), BaseMangaGetResolver {

    companion object {
        val INSTANCE = LibraryMangaGetResolver()
    }

    override fun mapFromCursor(cursor: Cursor): LibraryManga {
        val manga = LibraryManga()

        mapBaseFromCursor(manga, cursor)
        manga.unread = cursor.getInt(cursor.getColumnIndex(MangaTable.COL_UNREAD))
        manga.category = cursor.getInt(cursor.getColumnIndex(MangaTable.COL_CATEGORY))

        return manga
    }

}
