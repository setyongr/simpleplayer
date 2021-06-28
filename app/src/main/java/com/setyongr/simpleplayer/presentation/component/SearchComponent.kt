package com.setyongr.simpleplayer.presentation.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.setyongr.simpleplayer.core.KeyedHash
import com.setyongr.simpleplayer.core.ui.MutableComponent
import com.setyongr.simpleplayer.databinding.SearchComponentBinding

/**
 * Component that used for searching
 */
class SearchComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MutableComponent<SearchComponent.State> {

    private var binding: SearchComponentBinding =
        SearchComponentBinding.inflate(LayoutInflater.from(context), this, true)

    override var state: State = State()

    data class State(
        var text: String = "",
        var onTextChanged: KeyedHash<((CharSequence) -> Unit)?> = KeyedHash(null)
    )


    var listener: TextWatcher? = null

    init {
        binding.ivClear.setOnClickListener {
            binding.editSearch.setText("")
            binding.editSearch.clearFocus()
        }

        binding.editSearch.addTextChangedListener {
            binding.ivClear.isVisible = it?.isNotEmpty() == true
        }
    }


    override fun bind(state: State) {
        binding.editSearch.removeTextChangedListener(listener)
        listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                state.onTextChanged.getValue()?.invoke(s ?: "")
            }
        }
        binding.editSearch.setText(state.text)
        binding.editSearch.addTextChangedListener(listener)
    }

    override fun unbind() {
        binding.editSearch.removeTextChangedListener(listener)
    }

    override fun getView(): View = this
}
