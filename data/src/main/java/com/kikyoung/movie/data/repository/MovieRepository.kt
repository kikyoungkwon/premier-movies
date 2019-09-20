package com.kikyoung.movie.data.repository

import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.data.storage.MoviesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MovieRepository(
    private val moviesDao: MoviesDao,
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getMovieList() = flow {
        var movieList = moviesDao.loadMovies()
        if (movieList.isNotEmpty()) emit(movieList)
        movieList = movieMapper.toMovieList(movieService.topRated())
        moviesDao.saveMovies(movieList)
        emit(moviesDao.loadMovies())
    }.flowOn(ioDispatcher)

    suspend fun getMovie(id: Int): Movie? = withContext(ioDispatcher) {
        var movieList = moviesDao.loadMovies()
        if (movieList.isEmpty()) movieList = movieMapper.toMovieList(movieService.topRated())
        movieList.find { it.id == id }
    }
}