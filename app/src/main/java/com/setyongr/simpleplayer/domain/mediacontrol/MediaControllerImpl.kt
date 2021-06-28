package com.setyongr.simpleplayer.domain.mediacontrol

import android.media.MediaPlayer
import com.setyongr.simpleplayer.domain.model.TrackResult
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import javax.inject.Inject

class MediaControllerImpl(
    private val mediaPlayer: MediaPlayer
) : MediaController {

    init {
        mediaPlayer.setOnCompletionListener {
            if (isMediaPrepared) next()
        }
    }

    // In reality, should be from database and only store current item.
    // Storing playlist in memory will waste so much space
    private val _playlist = MutableStateFlow<List<TrackResult>>(emptyList())
    override val playlist: StateFlow<List<TrackResult>> = _playlist

    private val _currentItem = MutableStateFlow(-1)
    override val currentItem: StateFlow<Int> = _currentItem

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Stop)
    override val mediaState: StateFlow<MediaState> = _mediaState

    override val timeInfo: Flow<TimeInfo> = flow {
        while (currentCoroutineContext().isActive) {
            if (mediaPlayer.isPlaying) {
                emit(
                    TimeInfo(
                        duration = mediaPlayer.duration,
                        position = mediaPlayer.currentPosition
                    )
                )
            }

            delay(TIMER_INFO_DELAY)
        }
    }

    private val getCurrentTrack: TrackResult?
        get() = _playlist.value.firstOrNull { it.trackId == _currentItem.value }

    private var isMediaPrepared: Boolean = false

    override fun setState(mediaState: MediaState) {
        _mediaState.value = mediaState
    }

    override fun setPlaylist(list: List<TrackResult>) {
        _playlist.value = list
    }

    override fun setPosition(position: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(position)
        }
    }

    override fun setTrack(trackResult: TrackResult) {
        _currentItem.value = trackResult.trackId
    }

    override fun play(reset: Boolean) {
        getCurrentTrack?.let {
            if (reset) {
                isMediaPrepared = false
                mediaPlayer.reset()
                mediaPlayer.setDataSource(it.previewUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    isMediaPrepared = true
                    it.start()
                }
            }

            if (isMediaPrepared) mediaPlayer.start()
            _mediaState.value = MediaState.Play
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        _mediaState.value = MediaState.Pause
    }

    override fun next() {
        val currentIndex = _playlist.value.indexOfFirst { it.trackId == _currentItem.value }
        if (currentIndex < _playlist.value.size - 1) {
            _currentItem.value = _playlist.value[currentIndex + 1].trackId
            play(reset = true)
        }
    }

    override fun prev() {
        val currentIndex = _playlist.value.indexOfFirst { it.trackId == _currentItem.value }
        if (currentIndex > 0) {
            _currentItem.value = _playlist.value[currentIndex - 1].trackId
            play(reset = true)
        }
    }

    companion object {
        const val TIMER_INFO_DELAY = 300L
    }
}