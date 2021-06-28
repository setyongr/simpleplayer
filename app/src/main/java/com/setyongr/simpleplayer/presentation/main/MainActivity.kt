package com.setyongr.simpleplayer.presentation.main

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyongr.simpleplayer.R
import com.setyongr.simpleplayer.core.data.Load
import com.setyongr.simpleplayer.core.data.errorMessage
import com.setyongr.simpleplayer.core.keyedHash
import com.setyongr.simpleplayer.core.threading.AppDispatcher
import com.setyongr.simpleplayer.core.ui.adapter.ComponentAsyncAdapter
import com.setyongr.simpleplayer.core.ui.adapter.ComponentItems
import com.setyongr.simpleplayer.core.unitListener
import com.setyongr.simpleplayer.databinding.ActivityMainBinding
import com.setyongr.simpleplayer.presentation.component.EmptyStateComponent
import com.setyongr.simpleplayer.presentation.component.LoadingComponent
import com.setyongr.simpleplayer.presentation.component.TrackComponent
import com.setyongr.simpleplayer.domain.mediacontrol.MediaState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ComponentAsyncAdapter

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var appDispatcher: AppDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        adapter = ComponentAsyncAdapter(lifecycleScope, appDispatcher)

        setupSearch()
        setupRecyclerView()
        setupMediaControlLayout()

        observerRender()
    }

    private fun setupSearch() {
        binding.searchComponent.mutateState {
            onTextChanged = keyedHash {
                viewModel.setSearchTerm(it.toString())
            }
        }

        viewModel.searchTerm.observe(this) {
            viewModel.fetchTrack(it)
        }
    }

    private fun setupRecyclerView() {
        binding.content.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupMediaControlLayout() {
        val mediaControlComponent = binding.content.mediaControlComponent

        viewModel.mediaState.observe(this) {
            mediaControlComponent.isVisible =
                it is MediaState.Play || it is MediaState.Pause

            mediaControlComponent.mutateState {
                isPlaying = it is MediaState.Play
            }
        }

        viewModel.timeInfo.observe(this) {
            mediaControlComponent.mutateState {
                maxProgress = it.duration
                currentProgress = it.position
            }
        }

        mediaControlComponent.mutateState {
            onProgressChanged = keyedHash { progress ->
                viewModel.setPosition(progress)
            }
            onPlayClick = unitListener {
                viewModel.playPause()
            }
            onNextClick = unitListener {
                viewModel.next()
            }
            onPrevClick = unitListener {
                viewModel.prev()
            }
        }
    }

    private fun observerRender() {
        viewModel.fetchTrackLoad.observe(this) { renderContent() }
        viewModel.trackList.observe(this) { renderContent() }
        viewModel.currentItem.observe(this) { renderContent() }
    }

    private fun renderContent() = adapter.setItems {
        when (val it = viewModel.fetchTrackLoad.value) {
            is Load.Success -> {
                renderTrack()
            }
            is Load.Loading -> {
                renderLoading()
            }
            is Load.Fail -> {
                renderFail(it.error)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun ComponentItems.renderTrack() {
        if (viewModel.trackList.value?.size == 0) {
            ::EmptyStateComponent.newItem(
                ID_EMPTY, EmptyStateComponent.State(
                    title = getString(R.string.empty_title),
                    description = getString(R.string.empty_description)
                )
            )
        } else {
            viewModel.trackList.value?.forEach { trackResult ->
                ::TrackComponent.newItem(
                    trackResult.trackId.toString(), TrackComponent.State(
                        isPlaying = trackResult.trackId == viewModel.currentItem.value,
                        trackResult = trackResult,
                        onClickListener = unitListener {
                            viewModel.playTrack(trackResult)
                        }
                    )
                )
            }
        }
    }

    private fun ComponentItems.renderLoading() {
        ::LoadingComponent.newItem(ID_LOADING, Unit)
    }

    private fun ComponentItems.renderFail(err: Throwable) {
        ::EmptyStateComponent.newItem(
            ID_FAIL, EmptyStateComponent.State(
                title = getString(R.string.error_title),
                description = err.errorMessage()
            )
        )
    }

    companion object {
        const val ID_EMPTY = "empty"
        const val ID_LOADING = "loading"
        const val ID_FAIL = "failed"
    }
}