package com.setyongr.simpleplayer.domain.mediacontrol

import com.setyongr.simpleplayer.domain.model.TrackResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaController {
    val mediaState: StateFlow<MediaState>
    val playlist: StateFlow<List<TrackResult>>
    val currentItem: StateFlow<Int>
    val timeInfo: Flow<TimeInfo>

    fun setState(mediaState: MediaState)
    fun setPlaylist(list: List<TrackResult>)
    fun setTrack(trackResult: TrackResult)
    fun setPosition(position: Int)
    fun play(reset: Boolean = false)
    fun pause()
    fun next()
    fun prev()
}