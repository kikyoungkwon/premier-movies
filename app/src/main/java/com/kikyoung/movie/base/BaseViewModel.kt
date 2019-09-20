package com.kikyoung.movie.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.exception.ServerException
import com.kikyoung.movie.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(private val uiDispatcher: CoroutineDispatcher) : ViewModel(),
    CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = (uiDispatcher + job)

    private val serverErrorLiveData = SingleLiveEvent<ServerException>()
    private val networkErrorLiveData = SingleLiveEvent<NetworkException>()

    fun handleRepositoryError(t: Throwable) =
        when (t) {
            // E.g. No Internet.
            is UnknownHostException, is TimeoutException -> networkErrorLiveData.postValue(
                NetworkException(t.message)
            )
            is ServerException -> serverErrorLiveData.postValue(t)
            else -> serverErrorLiveData.postValue(ServerException(t.message))
        }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    fun serverErrorLiveData(): LiveData<ServerException> = serverErrorLiveData
    fun networkErrorLiveData(): LiveData<NetworkException> = networkErrorLiveData
}