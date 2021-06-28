package com.setyongr.simpleplayer.core.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.setyongr.simpleplayer.core.threading.AppDispatcher
import com.setyongr.simpleplayer.core.ui.Component
import kotlinx.coroutines.CoroutineScope

/**
 * Adapter for component with async diff utils
 */
class ComponentAsyncAdapter(
    coroutineScope: CoroutineScope,
    appDispatcher: AppDispatcher
) :
    RecyclerView.Adapter<ComponentAsyncAdapter.ComponentViewHolder<Any>>() {

    private var componentItemsStore = ComponentItemsStore(coroutineScope, appDispatcher, this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder<Any> {
        val componentBuilder = componentItemsStore.currentComponentItems
            .componentBuilder.getValue(viewType)
        val component = componentBuilder(parent.context)
        return ComponentViewHolder(component)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder<Any>, position: Int) {
        holder.bindState(componentItemsStore.currentComponentItems.items[position].state)
    }

    override fun getItemViewType(position: Int): Int =
        componentItemsStore.currentComponentItems.items[position].viewType

    override fun getItemCount(): Int = componentItemsStore.currentComponentItems.items.count()

    override fun onViewRecycled(holder: ComponentViewHolder<Any>) {
        holder.unbind()
    }

    fun set(newComponentItems: ComponentItems) {
        componentItemsStore.update(newComponentItems)
    }

    fun setItems(block: ComponentItems.() -> Unit) {
        set(ComponentItems().also(block))
    }

    class ComponentViewHolder<S : Any>(
        private val component: Component<S>
    ) : RecyclerView.ViewHolder(component.getView()) {
        fun bindState(state: S) {
            component.bind(state)
        }

        fun unbind() {
            component.unbind()
        }
    }
}
