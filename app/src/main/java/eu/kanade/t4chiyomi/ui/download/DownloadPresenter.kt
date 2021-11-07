package eu.kanade.t4chiyomi.ui.download

import android.os.Bundle
import eu.kanade.t4chiyomi.data.download.DownloadManager
import eu.kanade.t4chiyomi.data.download.model.Download
import eu.kanade.t4chiyomi.data.download.model.DownloadQueue
import eu.kanade.t4chiyomi.ui.base.presenter.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import uy.kohesive.injekt.injectLazy
import java.util.*

/**
 * Presenter of [DownloadController].
 */
class DownloadPresenter : BasePresenter<DownloadController>() {

    /**
     * Download manager.
     */
    val downloadManager: DownloadManager by injectLazy()

    /**
     * Property to get the queue from the download manager.
     */
    val downloadQueue: DownloadQueue
        get() = downloadManager.queue

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        downloadQueue.getUpdatedObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { ArrayList(it) }
                .subscribeLatestCache(DownloadController::onNextDownloads, { _, error ->
                    Timber.e(error)
                })
    }

    fun getDownloadStatusObservable(): Observable<Download> {
        return downloadQueue.getStatusObservable()
                .startWith(downloadQueue.getActiveDownloads())
    }

    fun getDownloadProgressObservable(): Observable<Download> {
        return downloadQueue.getProgressObservable()
                .onBackpressureBuffer()
    }

    /**
     * Pauses the download queue.
     */
    fun pauseDownloads() {
        downloadManager.pauseDownloads()
    }

    /**
     * Clears the download queue.
     */
    fun clearQueue() {
        downloadManager.clearQueue()
    }

}