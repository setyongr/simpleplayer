package com.setyongr.simpleplayer.core.threading

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatcher {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}