package eu.kanade.t4chiyomi.ui.reader.loader

import android.app.Application
import eu.kanade.t4chiyomi.data.database.models.Manga
import eu.kanade.t4chiyomi.data.download.DownloadManager
import eu.kanade.t4chiyomi.source.Source
import eu.kanade.t4chiyomi.source.model.Page
import eu.kanade.t4chiyomi.ui.reader.model.ReaderChapter
import eu.kanade.t4chiyomi.ui.reader.model.ReaderPage
import rx.Observable
import uy.kohesive.injekt.injectLazy

/**
 * Loader used to load a chapter from the downloaded chapters.
 */
class DownloadPageLoader(
        private val chapter: ReaderChapter,
        private val manga: Manga,
        private val source: Source,
        private val downloadManager: DownloadManager
) : PageLoader() {

    /**
     * The application context. Needed to open input streams.
     */
    private val context by injectLazy<Application>()

    /**
     * Returns an observable containing the pages found on this downloaded chapter.
     */
    override fun getPages(): Observable<List<ReaderPage>> {
        return downloadManager.buildPageList(source, manga, chapter.chapter)
            .map { pages ->
                pages.map { page ->
                    ReaderPage(page.index, page.url, page.imageUrl) {
                        context.contentResolver.openInputStream(page.uri)
                    }.apply {
                        status = Page.READY
                    }
                }
            }
    }

    override fun getPage(page: ReaderPage): Observable<Int> {
        return Observable.just(Page.READY) // TODO maybe check if file still exists?
    }

}
