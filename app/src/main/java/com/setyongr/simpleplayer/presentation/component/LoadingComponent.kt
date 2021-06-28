package com.setyongr.simpleplayer.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.setyongr.simpleplayer.core.ui.Component
import com.setyongr.simpleplayer.databinding.LoadingComponentBinding

/**
 * Component that represent loading
 */
class LoadingComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Component<Unit> {

    private var binding: LoadingComponentBinding =
        LoadingComponentBinding.inflate(LayoutInflater.from(context), this, true)

    override fun bind(state: Unit) {}

    override fun unbind() {}

    override fun getView(): View = this

    override fun prepareAsItem() {
        layoutParams = MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }
}