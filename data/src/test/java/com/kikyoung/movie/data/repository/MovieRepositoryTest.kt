package com.kikyoung.movie.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.data.model.TopRatedResponse
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.data.storage.MoviesDao
import com.kikyoung.movie.data.util.TestUtil
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MovieRepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val moviesDao = mockk<MoviesDao>(relaxed = true)
    private val movieMapper = mockk<MovieMapper>(relaxed = true)
    private val movieService = mockk<MovieService>(relaxed = true)
    @ExperimentalCoroutinesApi
    private val ioDispatcher = Dispatchers.Unconfined

    @Test
    fun `when getting a movie list after saved a movie list to local storage, it should get from local storage`() = runBlocking {
        val localMovieList = TestUtil.createMovies(1)
        coEvery { moviesDao.loadMovies() } returns localMovieList
        coEvery { movieService.topRated() } returns mockk()
        val removeMovieList = mockk<List<Movie>>()
        every { movieMapper.toMovieList(any()) } returns removeMovieList
        val movieRepository = MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher)
        assertEquals(localMovieList, movieRepository.getMovieList().first())
    }

    @Test
    fun `when getting a movie list from remote, it should map to movie list`() = runBlocking {
        coEvery { moviesDao.loadMovies() } returns listOf()
        val moviesResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns moviesResponse
        MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher).getMovieList().collect()
        verify(exactly = 1) { movieMapper.toMovieList(moviesResponse) }
    }

    @Test
    fun `when getting a movie list from remote is successful, it should provide the list`() = runBlocking {
        val topRatedResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns topRatedResponse
        val movieList = mockk<List<Movie>>(relaxed = true)
        every { movieMapper.toMovieList(topRatedResponse) } returns movieList
        coEvery { moviesDao.loadMovies() } returnsMany listOf(listOf(), movieList)
        val movieRepository = MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher)
        assertEquals(movieList, movieRepository.getMovieList().first())
    }

    @Test
    fun `when getting a movie list from remote is successful, it should save locally`() = runBlocking {
        coEvery { moviesDao.loadMovies() } returns listOf()
        val topRatedResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns topRatedResponse
        val movieList = mockk<List<Movie>>(relaxed = true)
        every { movieMapper.toMovieList(topRatedResponse) } returns movieList
        MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher).getMovieList().collect()
        coVerify(exactly = 1) { moviesDao.saveMovies(movieList) }
    }

    @Test(expected = NetworkException::class)
    fun `when getting a movie list from remote throws an exception, it should throw it`() = runBlocking {
        coEvery { moviesDao.loadMovies() } returns listOf()
        val exception = NetworkException("network error")
        coEvery { movieService.topRated() } throws exception
        MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher).getMovieList().collect()
        Unit
    }

    @Test
    fun `when getting a movie is successful, it should provide it`() = runBlocking {
        val movieIndex = 0
        val movieList = TestUtil.createMovies(1)
        coEvery { moviesDao.loadMovies() } returns movieList
        val movie = MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher).getMovie(movieList[movieIndex].id)
        assertEquals(movieList[movieIndex].id, movie?.id)
    }

    @Test(expected = NetworkException::class)
    fun `when getting a movie throws an exception, it should throw it`() = runBlocking {
        coEvery { moviesDao.loadMovies() } returns listOf()
        val exception = NetworkException("network error")
        coEvery { movieService.topRated() } throws exception
        MovieRepository(moviesDao, movieMapper, movieService, ioDispatcher).getMovie(0)
        Unit
    }
}