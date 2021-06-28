package com.setyongr.simpleplayer.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes

abstract class ResComponentItem<S : Any>(context: Context, @LayoutRes res: Int) : Component<S> {
    private val itemView: View = LayoutInflater.from(context).inflate(res, null, false)
    override fun getView(): View = itemView
}