package com.setyongr.simpleplayer.domain.model

data class TrackResult(
    val trackId: Int,
    val artistName: String,
    val trackName: String,
    val collectionName: String,
    val artworkUrl100: String?,
    val previewUrl: String
)