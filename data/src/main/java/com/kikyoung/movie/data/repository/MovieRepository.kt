package com.kikyoung.movie.data.repository

import androidx.annotation.VisibleForTesting
import com.kikyoung.movie.data.LocalStorage
import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.feature.list.model.Movie
import com.squareup.moshi.Types.newParameterizedType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieRepository(
    private val localStorage: LocalStorage,
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        @VisibleForTesting
        const val KEY_MOVIE_LIST = "movie_list"
    }

    // NOTE Use Coroutines Flow for cold stream.
    suspend fun getMovieList(): List<Movie> = withContext(ioDispatcher) {
        var movieList = getMovieListLocally()
        if (movieList == null) {
            movieList = movieMapper.toMovieList(movieService.topRated())
            putMovieListLocally(movieList)
        }
        movieList
    }!!

    suspend fun getMovie(id: Int): Movie? = withContext(ioDispatcher) {
        getMovieList().find { it.id == id }
    }

    private fun getMovieListLocally(): List<Movie>? =
        localStorage.get(KEY_MOVIE_LIST, newParameterizedType(List::class.java, Movie::class.java))

    private fun putMovieListLocally(movieList: List<Movie>) =
        localStorage.put(
            KEY_MOVIE_LIST,
            newParameterizedType(List::class.java, Movie::class.java),
            movieList
        )
}