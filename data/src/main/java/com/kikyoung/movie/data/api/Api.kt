package com.kikyoung.movie.data.api

import com.kikyoung.movie.data.model.TopRatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("3/movie/top_rated")
    suspend fun topRated(@Query("api_key") apiKey: String): Response<TopRatedResponse>
}