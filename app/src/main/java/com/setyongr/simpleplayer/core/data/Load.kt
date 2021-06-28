package com.setyongr.simpleplayer.core.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class Load<out T> {
    private var consumed: Boolean = false

    object Uninitialized : Load<Nothing>()
    object Loading : Load<Nothing>()
    data class Fail(val error: Throwable) : Load<Nothing>()
    data class Success<T : Any>(val data: T) : Load<T>()

    fun successData(): T? = (this as? Success)?.data

    fun oneShoot(block: () -> Unit) {
        if (!consumed) {
            block()
            consumed = true
        }
    }

    companion object {
        fun <T> liveData(): MutableLiveData<Load<T>> {
            return MutableLiveData<Load<T>>().apply { value = Uninitialized }
        }
    }
}

fun <T : Any> load(block: suspend () -> T): Flow<Load<T>> = flow {
    emit(Load.Loading)
    tryNetwork(
        block = {
            val data = block()
            emit(Load.Success(data))
        },
        onNetworkException = {
            emit(Load.Fail(it))
        }
    )
}

suspend fun <T : Any> MutableLiveData<Load<T>>.load(block: suspend () -> T) {
    postValue(Load.Loading)
    tryNetwork(
        block = {
            val data = block()
            postValue(Load.Success(data))
        },
        onNetworkException = {
            postValue(Load.Fail(it))
        }
    )
}

class JobLoaderLiveData<T : Any>(
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
    private val cancelActiveOnReload: Boolean = false
) : MutableLiveData<Load<T>>(Load.Uninitialized) {
    private var job: Job? = null

    fun loadData(cancelExisting: Boolean = cancelActiveOnReload, block: suspend () -> Unit) {
        if (cancelExisting) {
            job?.cancel()
        } else if (job?.isActive == true) {
            return
        }

        job = coroutineScope.launch(dispatcher) { block() }
    }

    fun cancelAndReset() {
        job?.cancel()
        postValue(Load.Uninitialized)
    }
}
