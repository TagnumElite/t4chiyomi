package eu.kanade.t4chiyomi.ui.reader.loader

import eu.kanade.t4chiyomi.source.model.Page
import eu.kanade.t4chiyomi.ui.reader.model.ReaderPage
import eu.kanade.t4chiyomi.util.ComparatorUtil.CaseInsensitiveNaturalComparator
import eu.kanade.t4chiyomi.util.ImageUtil
import rx.Observable
import java.io.File
import java.io.FileInputStream

/**
 * Loader used to load a chapter from a directory given on [file].
 */
class DirectoryPageLoader(val file: File) : PageLoader() {

    /**
     * Returns an observable containing the pages found on this directory ordered with a natural
     * comparator.
     */
    override fun getPages(): Observable<List<ReaderPage>> {
        return file.listFiles()
            .filter { !it.isDirectory && ImageUtil.isImage(it.name) { FileInputStream(it) } }
            .sortedWith(Comparator<File> { f1, f2 -> CaseInsensitiveNaturalComparator.compare(f1.name, f2.name) })
            .mapIndexed { i, file ->
                val streamFn = { FileInputStream(file) }
                ReaderPage(i).apply {
                    stream = streamFn
                    status = Page.READY
                }
            }
            .let { Observable.just(it) }
    }

    /**
     * Returns an observable that emits a ready state.
     */
    override fun getPage(page: ReaderPage): Observable<Int> {
        return Observable.just(Page.READY)
    }

}
