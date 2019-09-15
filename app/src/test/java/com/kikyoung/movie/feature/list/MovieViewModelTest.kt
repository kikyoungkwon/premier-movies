package com.kikyoung.movie.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.exception.ServerException
import com.kikyoung.movie.data.repository.MovieRepository
import com.kikyoung.movie.feature.list.model.Movie
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.net.UnknownHostException
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var movieRepository: MovieRepository = mockk(relaxed = true)
    private var viewModel = MovieViewModel(movieRepository, Dispatchers.Unconfined)

    @Test
    fun `when getting a movie list is successful, it should provide it`() = runBlocking {
        val movieList = mockk<List<Movie>>()
        coEvery { movieRepository.getMovieList() } returns movieList
        val observer = mockk<Observer<List<Movie>>>(relaxed = true)
        viewModel.movieListLiveData().observeForever(observer)
        viewModel.getMovieList()
        verify(exactly = 1) { observer.onChanged(movieList) }
    }

    @Test
    fun `when getting a movie list, it should show and hide the loading bar`() = runBlocking {
        coEvery { movieRepository.getMovieList() } returns mockk()
        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.loadingLiveData().observeForever(observer)
        viewModel.getMovieList()
        verifySequence {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `when getting a movie list is unsuccessful with a server error, it should show the error`() =
        runBlocking {
            val serverException = mockk<ServerException>()
            coEvery { movieRepository.getMovieList() } throws serverException
            val observer = mockk<Observer<ServerException>>(relaxed = true)
            viewModel.serverErrorLiveData().observeForever(observer)
            viewModel.getMovieList()
            verify(exactly = 1) { observer.onChanged(serverException) }
        }

    @Test
    fun `when getting a movie list is unsuccessful with a network error, it should show the error`() {
        val message = "unknownHostException"
        val networkException = UnknownHostException(message)
        coEvery { movieRepository.getMovieList() } throws networkException
        val observer = mockk<Observer<NetworkException>>(relaxed = true)
        viewModel.networkErrorLiveData().observeForever(observer)
        viewModel.getMovieList()
        val slot = slot<NetworkException>()
        verify(exactly = 1) { observer.onChanged(capture(slot)) }
        assertEquals(slot.captured.message, message)
    }

    @Test
    fun `when getting a movie is successful, it should provide it`() = runBlocking {
        val id = 0
        val movie = mockk<Movie>()
        coEvery { movieRepository.getMovie(id) } returns movie
        val observer = mockk<Observer<Movie?>>(relaxed = true)
        viewModel.movieLiveData().observeForever(observer)
        viewModel.getMovie(id)
        verify(exactly = 1) { observer.onChanged(movie) }
    }

    @Test
    fun `when getting a movie, it should show and hide the loading bar`() = runBlocking {
        val id = 0
        coEvery { movieRepository.getMovie(id) } returns mockk()
        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.loadingLiveData().observeForever(observer)
        viewModel.getMovie(id)
        verifySequence {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `when getting a movie is unsuccessful with a server error, it should show the error`() =
        runBlocking {
            val id = 0
            val serverException = mockk<ServerException>()
            coEvery { movieRepository.getMovie(id) } throws serverException
            val observer = mockk<Observer<ServerException>>(relaxed = true)
            viewModel.serverErrorLiveData().observeForever(observer)
            viewModel.getMovie(id)
            verify(exactly = 1) { observer.onChanged(serverException) }
        }

    @Test
    fun `when getting a movie is unsuccessful with a network error, it should show the error`() {
        val id = 0
        val message = "unknownHostException"
        val networkException = UnknownHostException(message)
        coEvery { movieRepository.getMovie(id) } throws networkException
        val observer = mockk<Observer<NetworkException>>(relaxed = true)
        viewModel.networkErrorLiveData().observeForever(observer)
        viewModel.getMovie(id)
        val slot = slot<NetworkException>()
        verify(exactly = 1) { observer.onChanged(capture(slot)) }
        assertEquals(slot.captured.message, message)
    }
}