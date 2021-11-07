package eu.kanade.t4chiyomi.ui.reader.loader

import eu.kanade.t4chiyomi.source.model.Page
import eu.kanade.t4chiyomi.ui.reader.model.ReaderPage
import eu.kanade.t4chiyomi.util.EpubFile
import rx.Observable
import java.io.File

/**
 * Loader used to load a chapter from a .epub file.
 */
class EpubPageLoader(file: File) : PageLoader() {

    /**
     * The epub file.
     */
    private val epub = EpubFile(file)

    /**
     * Recycles this loader and the open zip.
     */
    override fun recycle() {
        super.recycle()
        epub.close()
    }

    /**
     * Returns an observable containing the pages found on this zip archive ordered with a natural
     * comparator.
     */
    override fun getPages(): Observable<List<ReaderPage>> {
        return epub.getImagesFromPages()
            .mapIndexed { i, path ->
                val streamFn = { epub.getInputStream(epub.getEntry(path)!!) }
                ReaderPage(i).apply {
                    stream = streamFn
                    status = Page.READY
                }
            }
            .let { Observable.just(it) }
    }

    /**
     * Returns an observable that emits a ready state unless the loader was recycled.
     */
    override fun getPage(page: ReaderPage): Observable<Int> {
        return Observable.just(if (isRecycled) {
            Page.ERROR
        } else {
            Page.READY
        })
    }

}
