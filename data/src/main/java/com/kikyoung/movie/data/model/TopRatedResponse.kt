package com.kikyoung.movie.data.model

import com.squareup.moshi.Json

data class TopRatedResponse(
    @field:Json(name = "results")
    val movies: List<MovieResponse>
)