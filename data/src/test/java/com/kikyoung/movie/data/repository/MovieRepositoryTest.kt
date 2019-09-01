package com.kikyoung.movie.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.model.TopRatedResponse
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.feature.list.model.Movie
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MovieRepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val movieMapper = mockk<MovieMapper>(relaxed = true)
    private val movieService = mockk<MovieService>(relaxed = true)
    @ExperimentalCoroutinesApi
    private val ioDispatcher = Dispatchers.Unconfined

    @Test
    fun `when getting top rated movies, it should map to movie list`() = runBlocking {
        val moviesResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated(any()) } returns moviesResponse
        MovieRepository(movieMapper, movieService, ioDispatcher).topRatedMovies()
        verify { movieMapper.toMovieList(moviesResponse) }
    }

    @Test
    fun `when getting top rated movies is successful, it should provide the list`() = runBlocking {
        val topRatedResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated(any()) } returns topRatedResponse
        val movieList = mockk<List<Movie>>(relaxed = true)
        every { movieMapper.toMovieList(topRatedResponse) } returns movieList
        val movieRepository = MovieRepository(movieMapper, movieService, ioDispatcher)
        assertEquals(movieList, movieRepository.topRatedMovies())
    }

    @Test(expected = NetworkException::class)
    fun `when getting top rated movies throws an exception, it should throw it`() = runBlocking {
        val exception = NetworkException("network error")
        coEvery { movieService.topRated(any()) } throws exception
        MovieRepository(movieMapper, movieService, ioDispatcher).topRatedMovies()
        Unit
    }
}