package com.setyongr.simpleplayer.core.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.setyongr.simpleplayer.core.threading.AppDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

class ComponentItemsStore(
    coroutineScope: CoroutineScope,
    private val dispatcher: AppDispatcher,
    private val listUpdateCallback: ListUpdateCallback
) {
    constructor(
        coroutineScope: CoroutineScope,
        dispatcher: AppDispatcher,
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>
    ) : this(coroutineScope, dispatcher, SimpleUpdateCallback(adapter))

    internal sealed class UpdateListOperation {
        object Clear : UpdateListOperation()

        class Insert(val componentItems: ComponentItems) : UpdateListOperation()
    }

    internal class SimpleUpdateCallback(private val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) :
        ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val updateActor =
        coroutineScope.actor<UpdateListOperation>(dispatcher.main(), CONFLATED) {
            consumeEach {
                if (!isActive) return@actor

                val oldComponentItems = currentComponentItems

                when (it) {
                    UpdateListOperation.Clear -> {
                        clear(oldComponentItems.items.size)
                    }
                    is UpdateListOperation.Insert -> {
                        if (oldComponentItems.items.isEmpty()) {
                            insert(it.componentItems)
                        } else {
                            val callback = diffUtilCallback(oldComponentItems, it.componentItems)
                            update(it.componentItems, callback)
                        }
                    }
                }
            }
        }

    var currentComponentItems: ComponentItems = ComponentItems()
        private set

    fun update(newComponentItems: ComponentItems) {
        if (newComponentItems.items.isEmpty()) {
            updateActor.offer(UpdateListOperation.Clear)
        } else {
            updateActor.offer(UpdateListOperation.Insert(newComponentItems))
        }
    }

    private suspend fun clear(count: Int) {
        withContext(dispatcher.main()) {
            currentComponentItems = ComponentItems()
            listUpdateCallback.onRemoved(0, count)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun insert(newComponentItems: ComponentItems) {
        withContext(dispatcher.main()) {
            currentComponentItems = newComponentItems
            listUpdateCallback.onInserted(0, newComponentItems.items.size)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun update(newComponentItems: ComponentItems, callback: DiffUtil.Callback) {
        withContext(dispatcher.default()) {
            val result = DiffUtil.calculateDiff(callback)
            if (!coroutineContext.isActive) return@withContext
            latch(newComponentItems, result)
        }
    }

    private suspend fun latch(newComponentItems: ComponentItems, result: DiffUtil.DiffResult) {
        withContext(dispatcher.main()) {
            currentComponentItems = newComponentItems
            result.dispatchUpdatesTo(listUpdateCallback)
        }
    }

    private fun diffUtilCallback(
        oldComponentItems: ComponentItems,
        newComponentItems: ComponentItems
    ) =
        object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldComponentItems.items[oldItemPosition].id == newComponentItems.items[newItemPosition].id

            override fun getOldListSize(): Int = oldComponentItems.items.size

            override fun getNewListSize(): Int = newComponentItems.items.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldComponentItems.items[oldItemPosition] ==
                    newComponentItems.items[newItemPosition]
        }
}