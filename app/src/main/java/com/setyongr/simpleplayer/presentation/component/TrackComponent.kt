package com.setyongr.simpleplayer.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.setyongr.simpleplayer.core.UnitListener
import com.setyongr.simpleplayer.core.emptyUnitListener
import com.setyongr.simpleplayer.core.loadUrl
import com.setyongr.simpleplayer.core.ui.Component
import com.setyongr.simpleplayer.core.ui.MutableComponent
import com.setyongr.simpleplayer.databinding.TrackComponentBinding
import com.setyongr.simpleplayer.domain.model.TrackResult

class TrackComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Component<TrackComponent.State> {

    private var binding: TrackComponentBinding =
        TrackComponentBinding.inflate(LayoutInflater.from(context), this, true)

    data class State(
        var trackResult: TrackResult,
        var isPlaying: Boolean,
        var onClickListener: UnitListener = emptyUnitListener()
    )

    override fun prepareAsItem() {
        layoutParams = MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    override fun getView(): View = this

    override fun bind(state: State) {
        binding.tvArtist.text = state.trackResult.artistName
        binding.tvSongName.text = state.trackResult.trackName
        binding.tvAlbum.text = state.trackResult.collectionName
        binding.ivArtist.loadUrl(state.trackResult.artworkUrl100)
        binding.ivIndicator.isVisible = state.isPlaying

        binding.root.setOnClickListener { state.onClickListener.getValue()?.invoke() }
    }

    override fun unbind() {
        binding.root.setOnClickListener(null)
    }
}