package com.kikyoung.movie.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kikyoung.movie.data.LocalStorage
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.mapper.MovieMapper
import com.kikyoung.movie.data.model.TopRatedResponse
import com.kikyoung.movie.data.repository.MovieRepository.Companion.KEY_MOVIE_LIST
import com.kikyoung.movie.data.service.MovieService
import com.kikyoung.movie.feature.list.model.Movie
import com.squareup.moshi.Types
import io.mockk.*
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

    private val localStore = mockk<LocalStorage>(relaxed = true)
    private val movieMapper = mockk<MovieMapper>(relaxed = true)
    private val movieService = mockk<MovieService>(relaxed = true)
    @ExperimentalCoroutinesApi
    private val ioDispatcher = Dispatchers.Unconfined

    @Test
    fun `when getting a movie list after saved a movie list to local storage, it should get from local storage`() = runBlocking {
        val localMovieList = mockk<List<Movie>>()
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns localMovieList
        val movieRepository = MovieRepository(localStore, movieMapper, movieService, ioDispatcher)
        coVerify(exactly = 0) { movieService.topRated() }
        assertEquals(localMovieList, movieRepository.getMovieList())
    }

    @Test
    fun `when getting a movie list from remote, it should map to movie list`() = runBlocking {
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns null
        val moviesResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns moviesResponse
        MovieRepository(localStore, movieMapper, movieService, ioDispatcher).getMovieList()
        verify(exactly = 1) { movieMapper.toMovieList(moviesResponse) }
    }

    @Test
    fun `when getting a movie list from remote is successful, it should provide the list`() = runBlocking {
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns null
        val topRatedResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns topRatedResponse
        val movieList = mockk<List<Movie>>(relaxed = true)
        every { movieMapper.toMovieList(topRatedResponse) } returns movieList
        val movieRepository = MovieRepository(localStore, movieMapper, movieService, ioDispatcher)
        assertEquals(movieList, movieRepository.getMovieList())
    }

    @Test
    fun `when getting a movie list from remote is successful, it should save locally`() = runBlocking {
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns null
        val topRatedResponse = mockk<TopRatedResponse>(relaxed = true)
        coEvery { movieService.topRated() } returns topRatedResponse
        val movieList = mockk<List<Movie>>(relaxed = true)
        every { movieMapper.toMovieList(topRatedResponse) } returns movieList
        MovieRepository(localStore, movieMapper, movieService, ioDispatcher).getMovieList()
        verify(exactly = 1) { localStore.put(KEY_MOVIE_LIST,
            Types.newParameterizedType(List::class.java, Movie::class.java), movieList) }
    }

    @Test(expected = NetworkException::class)
    fun `when getting a movie list from remote throws an exception, it should throw it`() = runBlocking {
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns null
        val exception = NetworkException("network error")
        coEvery { movieService.topRated() } throws exception
        MovieRepository(localStore, movieMapper, movieService, ioDispatcher).getMovieList()
        Unit
    }

    @Test
    fun `when getting a movie is successful, it should provide it`() = runBlocking {
        val movieIndex = 0
        val movieList = listOf(Movie(0, "title0", "overview0", "http://localhost/post_path0.jpg"))
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns movieList
        val movie = MovieRepository(localStore, movieMapper, movieService, ioDispatcher).getMovie(movieList[movieIndex].id)
        assertEquals(movieList[movieIndex].id, movie?.id)
    }

    @Test(expected = NetworkException::class)
    fun `when getting a movie throws an exception, it should throw it`() = runBlocking {
        every { localStore.get<List<Movie>>(KEY_MOVIE_LIST, any()) } returns null
        val exception = NetworkException("network error")
        coEvery { movieService.topRated() } throws exception
        MovieRepository(localStore, movieMapper, movieService, ioDispatcher).getMovie(0)
        Unit
    }
}