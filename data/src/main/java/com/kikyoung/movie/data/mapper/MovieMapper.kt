package com.kikyoung.movie.data.mapper

import com.kikyoung.movie.data.model.TopRatedResponse
import com.kikyoung.movie.feature.list.model.Movie

class MovieMapper(private val imageBaseUrl: String) {

    fun toMovieList(topRatedResponse: TopRatedResponse): List<Movie> =
        topRatedResponse.movies.map { movieResponse ->
            Movie(
                movieResponse.title,
                movieResponse.overview,
                "$imageBaseUrl${movieResponse.posterFilePath}"
            )
        }
}