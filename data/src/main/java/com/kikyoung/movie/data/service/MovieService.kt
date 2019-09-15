package com.kikyoung.movie.data.service

import com.kikyoung.movie.data.api.Api
import com.kikyoung.movie.data.model.TopRatedResponse
import com.squareup.moshi.Moshi

class MovieService(
    private val apiKey: String,
    moshi: Moshi,
    private val api: Api
) : BaseService(moshi) {

    suspend fun topRated(): TopRatedResponse = execute { api.topRated(apiKey) }
}