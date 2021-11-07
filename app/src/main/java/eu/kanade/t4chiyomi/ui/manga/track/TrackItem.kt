package eu.kanade.t4chiyomi.ui.manga.track

import eu.kanade.t4chiyomi.data.database.models.Track
import eu.kanade.t4chiyomi.data.track.TrackService

data class TrackItem(val track: Track?, val service: TrackService)
