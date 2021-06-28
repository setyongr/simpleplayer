package com.setyongr.simpleplayer.core.data

import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.CancellationException

fun HttpException.getErrorMessage(): String {
    val errorCode = code()
    if (errorCode == 500) {
        return "Internal Server Error"
    } else if (errorCode == 404) {
        return "Not Found"
    }

    return "Terjadi Kesalahan"
}

fun Throwable.errorMessage(): String {
    if (this is HttpException) {
        return this.getErrorMessage()
    }

    if (this is IOException) {
        return "Koneksi Bermasalah"
    }

    return this.message ?: ""
}

fun Throwable.isServerError(): Boolean {
    if (this is HttpException) {
        return this.code() == 500
    }

    return false
}

suspend fun tryNetwork(
    block: suspend () -> Unit,
    onNetworkException: suspend (Exception) -> Unit,
    onOtherException: suspend (Exception) -> Unit = { throw it },
    ignoreCancellation: Boolean = true
) {
    try {
        block()
    } catch (e: HttpException) {
        onNetworkException(e)
    } catch (e: IOException) {
        onNetworkException(e)
    } catch (e: CancellationException) {
        if (!ignoreCancellation) {
            onOtherException(e)
        }
    } catch (e: Exception) {
        onOtherException(e)
    }
}
