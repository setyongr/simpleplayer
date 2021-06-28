package com.setyongr.simpleplayer.core.ui.adapter

import android.content.Context
import android.util.AttributeSet
import com.setyongr.simpleplayer.core.ui.Component
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction3

class ComponentItems {
    val componentBuilder: MutableMap<Int, (context: Context) -> Component<Any>> = mutableMapOf()
    val items: MutableList<Item> = mutableListOf()

    fun <S : Any> add(itemBuilder: ItemBuilder<S>) {
        val castedItemBuilder = itemBuilder as ItemBuilder<Any>
        componentBuilder[itemBuilder.viewType] = castedItemBuilder.builder
        items.add(
            Item(
                id = castedItemBuilder.id,
                viewType = castedItemBuilder.viewType,
                state = castedItemBuilder.state
            )
        )
    }

    @JvmName("newItem3")
    inline fun <S : Any, reified C : Component<S>>
        KFunction3<Context, AttributeSet?, Int, C>.newItem(
        id: String,
        state: S,
        viewType: Int = C::class.hashCode()
    ) {

        val itemBuilder = ItemBuilder(
            viewType = viewType,
            builder = { this(it, null, 0).apply { prepareAsItem() } },
            id = id,
            state = state
        )

        add(itemBuilder)
    }

    @JvmName("newItem")
    inline fun <S : Any, reified C : Component<S>>
        KFunction1<Context, C>.newItem(
        id: String,
        state: S,
        viewType: Int = C::class.hashCode()
    ) {

        val itemBuilder = ItemBuilder(
            viewType = viewType,
            builder = { this(it).apply { prepareAsItem() } },
            id = id,
            state = state
        )

        add(itemBuilder)
    }
    
    data class Item(val id: String, val viewType: Int, val state: Any)
}