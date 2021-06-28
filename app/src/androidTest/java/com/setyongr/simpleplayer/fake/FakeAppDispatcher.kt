package com.setyongr.simpleplayer.fake

import com.setyongr.simpleplayer.core.threading.AppDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class FakeAppDispatcher : AppDispatcher {
    private val dispatcher = TestCoroutineDispatcher()

    override fun io(): CoroutineDispatcher = dispatcher
    override fun main(): CoroutineDispatcher = dispatcher
    override fun default(): CoroutineDispatcher = dispatcher
}