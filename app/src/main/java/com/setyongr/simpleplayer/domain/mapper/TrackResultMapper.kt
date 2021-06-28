package com.setyongr.simpleplayer.domain.mapper

import com.setyongr.simpleplayer.data.model.ItunesResponse
import com.setyongr.simpleplayer.domain.model.TrackResult

fun ItunesResponse.mapToTrackResult(): List<TrackResult> {
    return this.results.map {
        TrackResult(
            trackId = it.trackId ?: 0,
            artistName = it.artistName ?: "",
            trackName = it.trackName ?: "",
            collectionName = it.collectionName ?: "",
            artworkUrl100 = it.artworkUrl100 ?: "",
            previewUrl = it.previewUrl ?: ""
        )
    }
}
