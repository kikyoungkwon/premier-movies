package com.kikyoung.movie.data.util

import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.data.model.MovieResponse
import com.kikyoung.movie.data.model.TopRatedResponse

object TestUtil {

    fun createMovies(count: Int) =
        (0 until count).map {
            createMovie(it)
        }

    fun createMovie(
        id: Int
    ) =
        Movie(id, "title$id", "overview$id", "https://posterUrl$id")

    fun createMoviesResponse(count: Int) =
        TopRatedResponse((0 until count).map {
            createMovieResponse(it)
        })

    fun createMovieResponse(
        id: Int
    ) =
        MovieResponse(id, "title$id", "overview$id", "porterFilePath$id")
}