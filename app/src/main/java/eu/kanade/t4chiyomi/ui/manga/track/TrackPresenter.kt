package eu.kanade.t4chiyomi.ui.manga.track

import android.os.Bundle
import eu.kanade.t4chiyomi.data.database.DatabaseHelper
import eu.kanade.t4chiyomi.data.database.models.Manga
import eu.kanade.t4chiyomi.data.database.models.Track
import eu.kanade.t4chiyomi.data.preference.PreferencesHelper
import eu.kanade.t4chiyomi.data.track.TrackManager
import eu.kanade.t4chiyomi.data.track.TrackService
import eu.kanade.t4chiyomi.ui.base.presenter.BasePresenter
import eu.kanade.t4chiyomi.util.toast
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get


class TrackPresenter(
        val manga: Manga,
        preferences: PreferencesHelper = Injekt.get(),
        private val db: DatabaseHelper = Injekt.get(),
        private val trackManager: TrackManager = Injekt.get()
) : BasePresenter<TrackController>() {

    private val context = preferences.context

    private var trackList: List<TrackItem> = emptyList()

    private val loggedServices by lazy { trackManager.services.filter { it.isLogged } }

    private var trackSubscription: Subscription? = null

    private var searchSubscription: Subscription? = null

    private var refreshSubscription: Subscription? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        fetchTrackings()
    }

    fun fetchTrackings() {
        trackSubscription?.let { remove(it) }
        trackSubscription = db.getTracks(manga)
                .asRxObservable()
                .map { tracks ->
                    loggedServices.map { service ->
                        TrackItem(tracks.find { it.sync_id == service.id }, service)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { trackList = it }
                .subscribeLatestCache(TrackController::onNextTrackings)
    }

    fun refresh() {
        refreshSubscription?.let { remove(it) }
        refreshSubscription = Observable.from(trackList)
                .filter { it.track != null }
                .concatMap { item ->
                    item.service.refresh(item.track!!)
                            .flatMap { db.insertTrack(it).asRxObservable() }
                            .map { item }
                            .onErrorReturn { item }
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeFirst({ view, _ -> view.onRefreshDone() },
                        TrackController::onRefreshError)
    }

    fun search(query: String, service: TrackService) {
        searchSubscription?.let { remove(it) }
        searchSubscription = service.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeLatestCache(TrackController::onSearchResults,
                        TrackController::onSearchResultsError)
    }

    fun registerTracking(item: Track?, service: TrackService) {
        if (item != null) {
            item.manga_id = manga.id!!
            add(service.bind(item)
                    .flatMap { db.insertTrack(item).asRxObservable() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ },
                            { error -> context.toast(error.message) }))
        } else {
            db.deleteTrackForManga(manga, service).executeAsBlocking()
        }
    }

    private fun updateRemote(track: Track, service: TrackService) {
        service.update(track)
                .flatMap { db.insertTrack(track).asRxObservable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeFirst({ view, _ -> view.onRefreshDone() },
                        { view, error ->
                            view.onRefreshError(error)

                            // Restart on error to set old values
                            fetchTrackings()
                        })
    }

    fun setStatus(item: TrackItem, index: Int) {
        val track = item.track!!
        track.status = item.service.getStatusList()[index]
        updateRemote(track, item.service)
    }

    fun setScore(item: TrackItem, index: Int) {
        val track = item.track!!
        track.score = item.service.indexToScore(index)
        updateRemote(track, item.service)
    }

    fun setLastChapterRead(item: TrackItem, chapterNumber: Int) {
        val track = item.track!!
        track.last_chapter_read = chapterNumber
        updateRemote(track, item.service)
    }

}