package com.kikyoung.movie.data.service

import com.kikyoung.movie.data.api.Api
import com.kikyoung.movie.data.model.TopRatedResponse
import com.squareup.moshi.Moshi

class MovieService(
    moshi: Moshi,
    private val api: Api
) : BaseService(moshi) {

    suspend fun topRated(apiKey: String): TopRatedResponse =
        execute { api.topRated(apiKey) }
}