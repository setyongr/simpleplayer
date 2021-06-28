package com.setyongr.simpleplayer.core.ui

import android.view.View

interface Component<S : Any> {
    fun bind(state: S)
    fun unbind()
    fun getView(): View
    fun prepareAsItem() {}
}

