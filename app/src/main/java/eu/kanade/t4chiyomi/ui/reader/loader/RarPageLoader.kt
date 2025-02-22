package eu.kanade.t4chiyomi.ui.reader.loader

import eu.kanade.t4chiyomi.source.model.Page
import eu.kanade.t4chiyomi.ui.reader.model.ReaderPage
import eu.kanade.t4chiyomi.util.ComparatorUtil.CaseInsensitiveNaturalComparator
import eu.kanade.t4chiyomi.util.ImageUtil
import junrar.Archive
import junrar.rarfile.FileHeader
import rx.Observable
import java.io.File
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.concurrent.Executors

/**
 * Loader used to load a chapter from a .rar or .cbr file.
 */
class RarPageLoader(file: File) : PageLoader() {

    /**
     * The rar archive to load pages from.
     */
    private val archive = Archive(file)

    /**
     * Pool for copying compressed files to an input stream.
     */
    private val pool = Executors.newFixedThreadPool(1)

    /**
     * Recycles this loader and the open archive.
     */
    override fun recycle() {
        super.recycle()
        archive.close()
        pool.shutdown()
    }

    /**
     * Returns an observable containing the pages found on this rar archive ordered with a natural
     * comparator.
     */
    override fun getPages(): Observable<List<ReaderPage>> {
        return archive.fileHeaders
            .filter { !it.isDirectory && ImageUtil.isImage(it.fileNameString) { archive.getInputStream(it) } }
            .sortedWith(Comparator<FileHeader> { f1, f2 -> CaseInsensitiveNaturalComparator.compare(f1.fileNameString, f2.fileNameString) })
            .mapIndexed { i, header ->
                val streamFn = { getStream(header) }

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

    /**
     * Returns an input stream for the given [header].
     */
    private fun getStream(header: FileHeader): InputStream {
        val pipeIn = PipedInputStream()
        val pipeOut = PipedOutputStream(pipeIn)
        pool.execute {
            try {
                pipeOut.use {
                    archive.extractFile(header, it)
                }
            } catch (e: Exception) {
            }
        }
        return pipeIn
    }

}
