package com.kikyoung.movie.data.service

import com.kikyoung.movie.data.exception.ServerException
import com.squareup.moshi.Moshi
import retrofit2.Response

open class BaseService(private val moshi: Moshi) {

    protected suspend fun <T> execute(
        handleError: ((response: Response<T>) -> Unit)? = null,
        serviceCall: suspend () -> Response<T>
    ): T {
        val response: Response<T> = serviceCall.invoke()
        if (!response.isSuccessful) {
            handleError?.invoke(response)
            throw moshi.adapter(ServerException::class.java).fromJson(response.errorBody().toString())
                ?: ServerException("empty error body")
        }
        return response.body() ?: throw ServerException("empty response body")
    }
}