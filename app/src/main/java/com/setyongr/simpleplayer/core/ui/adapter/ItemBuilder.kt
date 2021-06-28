package com.setyongr.simpleplayer.core.ui.adapter

import android.content.Context
import com.setyongr.simpleplayer.core.ui.Component

class ItemBuilder<S: Any>(
    val viewType: Int,
    val builder: (context: Context) -> Component<S>,
    val id: String,
    val state: S
)