package com.kikyoung.movie.data.repository

import androidx.lifecycle.MutableLiveData
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

    private val selectedMovieLiveData = MutableLiveData<Movie>()

    private var movieList: List<Movie>? = null

    suspend fun topRatedMovies(): List<Movie> = withContext(ioDispatcher) {
        movieList = movieMapper.toMovieList(movieService.topRated(API_KEY))
        // TODO Save the list in local storage so that can start with later
        movieList!!
    }

    fun setSelectedMovie(id: String) {
        val movie = movieList?.find { movie -> movie.id == id}
        if (movie != null) selectedMovieLiveData.postValue(movie)
        else {
            // TODO Get it from backend
        }
    }

    fun selectedMovieLiveData() = selectedMovieLiveData
}