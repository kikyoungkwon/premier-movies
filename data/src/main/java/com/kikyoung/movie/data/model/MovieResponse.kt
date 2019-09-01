package com.kikyoung.movie.data.model

import com.squareup.moshi.Json

data class MovieResponse(
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "overview")
    val overview: String,
    @field:Json(name = "poster_path")
    val posterFilePath: String
)