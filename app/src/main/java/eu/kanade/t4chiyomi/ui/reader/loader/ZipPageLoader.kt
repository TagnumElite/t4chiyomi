package eu.kanade.t4chiyomi.ui.reader.loader

import eu.kanade.t4chiyomi.source.model.Page
import eu.kanade.t4chiyomi.ui.reader.model.ReaderPage
import eu.kanade.t4chiyomi.util.ComparatorUtil.CaseInsensitiveNaturalComparator
import eu.kanade.t4chiyomi.util.ImageUtil
import rx.Observable
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Loader used to load a chapter from a .zip or .cbz file.
 */
class ZipPageLoader(file: File) : PageLoader() {

    /**
     * The zip file to load pages from.
     */
    private val zip = ZipFile(file)

    /**
     * Recycles this loader and the open zip.
     */
    override fun recycle() {
        super.recycle()
        zip.close()
    }

    /**
     * Returns an observable containing the pages found on this zip archive ordered with a natural
     * comparator.
     */
    override fun getPages(): Observable<List<ReaderPage>> {
        return zip.entries().toList()
            .filter { !it.isDirectory && ImageUtil.isImage(it.name) { zip.getInputStream(it) } }
            .sortedWith(Comparator<ZipEntry> { f1, f2 -> CaseInsensitiveNaturalComparator.compare(f1.name, f2.name) })
            .mapIndexed { i, entry ->
                val streamFn = { zip.getInputStream(entry) }
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
