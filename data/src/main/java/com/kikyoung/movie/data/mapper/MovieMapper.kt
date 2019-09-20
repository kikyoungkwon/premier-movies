package com.kikyoung.movie.data.mapper

import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.data.model.TopRatedResponse

class MovieMapper(private val imageBaseUrl: String) {

    fun toMovieList(topRatedResponse: TopRatedResponse): List<Movie> =
        topRatedResponse.movies.map { movieResponse ->
            Movie(
                movieResponse.id,
                movieResponse.title,
                movieResponse.overview,
                "$imageBaseUrl${movieResponse.posterFilePath}"
            )
        }
}