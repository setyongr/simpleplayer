package com.setyongr.simpleplayer.presentation.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.setyongr.simpleplayer.R
import com.setyongr.simpleplayer.core.KeyedHash
import com.setyongr.simpleplayer.core.UnitListener
import com.setyongr.simpleplayer.core.emptyUnitListener
import com.setyongr.simpleplayer.core.keyedHash
import com.setyongr.simpleplayer.core.ui.MutableComponent
import com.setyongr.simpleplayer.databinding.MediaControlComponentBinding
import com.setyongr.simpleplayer.databinding.SearchComponentBinding
import com.setyongr.simpleplayer.domain.mediacontrol.MediaState

class MediaControlComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MutableComponent<MediaControlComponent.State> {

    private var binding: MediaControlComponentBinding =
        MediaControlComponentBinding.inflate(LayoutInflater.from(context), this, true)

    override var state: State = State()

    data class State(
        var isPlaying: Boolean = false,
        var maxProgress: Int = 0,
        var currentProgress: Int = 0,
        var onProgressChanged: KeyedHash<((Int) -> Unit)?> = keyedHash(null),
        var onPlayClick: UnitListener = emptyUnitListener(),
        var onPrevClick: UnitListener = emptyUnitListener(),
        var onNextClick: UnitListener = emptyUnitListener()
    )

    override fun bind(state: State) {
        val playPauseResource = if (state.isPlaying) {
            R.drawable.ic_baseline_pause_circle_filled_24
        } else {
            R.drawable.ic_baseline_play_arrow_24
        }

        binding.ivPlayPause.setImageResource(playPauseResource)
        binding.seekBar.max = state.maxProgress
        binding.seekBar.progress = state.currentProgress
        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) state.onProgressChanged.getValue()?.invoke(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.ivPlayPause.setOnClickListener {
            state.onPlayClick.getValue()?.invoke()
        }

        binding.ivNext.setOnClickListener {
            state.onNextClick.getValue()?.invoke()
        }

        binding.ivPrev.setOnClickListener {
            state.onPrevClick.getValue()?.invoke()
        }
    }

    override fun unbind() {}

    override fun getView(): View = this
}
