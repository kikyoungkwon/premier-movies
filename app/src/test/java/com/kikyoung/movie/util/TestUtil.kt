package com.kikyoung.movie.util

import com.kikyoung.movie.data.model.Movie

// TODO Use same TestUtil class in data module test.
object TestUtil {

    fun createMovies(count: Int) =
        (0 until count).map {
            createMovie(it)
        }

    fun createMovie(
        id: Int
    ) =
        Movie(id, "title$id", "overview$id", "https://posterUrl$id")
}