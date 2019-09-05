package com.kikyoung.movie.data.repository

import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.feature.list.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        // TODO Get it from secure place
        private const val API_KEY = "e4f9e61f6ffd66639d33d3dde7e3159b"
    }

    suspend fun topRatedMovies(searchText: String? = "God"): List<Movie> =
        withContext(ioDispatcher) {
            val savedMovieList = getMovieListFromLocal()
            var movieList: List<Movie>? = null
            if(savedMovieList == null) {
                movieList = movieMapper.toMovieList(movieService.topRated(API_KEY))
                saveMovieListLocally(movieList)
            }
            var searchedList = getMovieListFromLocal()
            if (searchText != null) {
                searchedList = searchedList?.filter { it.title.contains(searchText) }
            }
            searchedList!!
        }

    fun saveMovieListLocally(movieList: List<Movie>) {}

    fun getMovieListFromLocal(): List<Movie>? {
        return null
    }
}