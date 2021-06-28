package com.setyongr.simpleplayer.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.setyongr.simpleplayer.core.ui.Component
import com.setyongr.simpleplayer.databinding.EmptyStateComponentBinding

/**
 * Component that represent empty state
 */
class EmptyStateComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Component<EmptyStateComponent.State> {

    private var binding: EmptyStateComponentBinding =
        EmptyStateComponentBinding.inflate(LayoutInflater.from(context), this, true)


    data class State(
        val title: String = "",
        val description: String = ""
    )

    override fun bind(state: State) {
        binding.textTitle.text = state.title
        binding.textDescription.text = state.description
    }

    override fun unbind() {}

    override fun getView(): View = this

    override fun prepareAsItem() {
        layoutParams = MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }
}