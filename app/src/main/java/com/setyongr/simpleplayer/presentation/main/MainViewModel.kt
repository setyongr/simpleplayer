package com.setyongr.simpleplayer.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.setyongr.simpleplayer.core.data.JobLoaderLiveData
import com.setyongr.simpleplayer.core.data.Load
import com.setyongr.simpleplayer.core.threading.AppDispatcher
import com.setyongr.simpleplayer.domain.cases.SearchTrackUseCase
import com.setyongr.simpleplayer.domain.model.TrackResult
import com.setyongr.simpleplayer.domain.mediacontrol.MediaController
import com.setyongr.simpleplayer.domain.mediacontrol.MediaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchTrackUseCase: SearchTrackUseCase,
    private val mediaController: MediaController,
    private val dispatcher: AppDispatcher
) : ViewModel() {
    private val _fetchTrackLoad =
        JobLoaderLiveData<List<TrackResult>>(viewModelScope, dispatcher.io(), true)
    private val _searchTermFlow = MutableStateFlow("")

    val fetchTrackLoad: LiveData<Load<List<TrackResult>>> = _fetchTrackLoad
    val trackList = mediaController.playlist.asLiveData(dispatcher.io())
    val currentItem = mediaController.currentItem.asLiveData(dispatcher.io())
    val mediaState = mediaController.mediaState.asLiveData(dispatcher.io())
    val timeInfo = mediaController.timeInfo.asLiveData(dispatcher.io())
    val searchTerm = _searchTermFlow.debounce(SEARCH_DEBOUNCE_TIME).asLiveData()

    /**
     * Fetch track from API with provided term
     *
     * @param term search term
     */
    fun fetchTrack(term: String) = _fetchTrackLoad.loadData {
        searchTrackUseCase(term).collect {
            if (it is Load.Success) {
                mediaController.setPlaylist(it.data)
            }
            _fetchTrackLoad.postValue(it)
        }
    }

    /**
     * Play selected track
     *
     * @param track track that want to be played
     */
    fun playTrack(track: TrackResult) {
        mediaController.setTrack(track)
        mediaController.play(true)
    }

    /**
     * Play / Pause currently played track
     */
    fun playPause() {
        if (mediaController.mediaState.value is MediaState.Play) {
            mediaController.pause()
        } else {
            mediaController.play()
        }
    }

    /**
     * Next track
     */
    fun next() {
        mediaController.next()
    }

    /**
     * Previous Track
     */
    fun prev() {
        mediaController.prev()
    }

    /**
     * Set time position of currently played track
     *
     * @param position time position
     */
    fun setPosition(position: Int) {
        mediaController.setPosition(position)
    }

    /**
     * Set search term that inserted by user
     *
     * @param searchTerm search term
     */
    fun setSearchTerm(searchTerm: String) {
        _searchTermFlow.value = searchTerm
    }

    companion object {
        const val SEARCH_DEBOUNCE_TIME = 1000L
    }
}