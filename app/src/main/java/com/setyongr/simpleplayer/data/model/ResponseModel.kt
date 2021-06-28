package com.setyongr.simpleplayer.data.model

data class ItunesResultResponse(
    val trackId: Int?,
    val artistName: String?,
    val trackName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
)

data class ItunesResponse(
    val resultCount: Int,
    val results: List<ItunesResultResponse>
)