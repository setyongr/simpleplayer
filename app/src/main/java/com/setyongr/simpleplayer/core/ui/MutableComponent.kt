package com.setyongr.simpleplayer.core.ui

interface MutableComponent<S : Any> : Component<S> {
    var state: S

    fun mutateState(mutation: S.() -> Unit) {
        mutation(state)
        bind(state)
    }

    fun mutateState(newState: S) {
        state = newState
        bind(state)
    }
}