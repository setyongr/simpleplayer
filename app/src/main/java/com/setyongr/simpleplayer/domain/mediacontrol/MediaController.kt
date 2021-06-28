package com.setyongr.simpleplayer.domain.mediacontrol

import com.setyongr.simpleplayer.domain.model.TrackResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MediaController {
    /**
     * State Flow that store current media player state
     */
    val mediaState: StateFlow<MediaState>

    /**
     * State flow that store current playlist
     */
    val playlist: StateFlow<List<TrackResult>>

    /**
     * State flow that store currently played track id
     */
    val currentItem: StateFlow<Int>

    /**
     * Flow that provide time info for currently played media.
     * It includes duration and current position
     */
    val timeInfo: Flow<TimeInfo>

    /**
     * Set media player state
     *
     * @param mediaState current media state
     */
    fun setState(mediaState: MediaState)

    /**
     * Set the playlist
     *
     * @param list playlist
     */
    fun setPlaylist(list: List<TrackResult>)

    /**
     * Set current track
     *
     * @param trackResult track that want to be player
     */
    fun setTrack(trackResult: TrackResult)

    /**
     * Set time position for currently played track
     */
    fun setPosition(position: Int)

    /**
     * Play selected track
     *
     * @param reset reset media player internal state
     */
    fun play(reset: Boolean = false)

    /**
     * Pause currently played track
     */
    fun pause()

    /**
     * Play next song on the playlist
     */
    fun next()

    /**
     * Play previous song on the playlist
     */
    fun prev()
}