package com.kikyoung.movie.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.exception.ServerException
import com.kikyoung.movie.data.repository.MovieRepository
import com.kikyoung.movie.feature.list.model.Movie
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var movieRepository: MovieRepository = mockk(relaxed = true)
    private var viewModel = MovieViewModel(movieRepository, Dispatchers.Unconfined)

    @Test
    fun `when getting movie list, it should try to get it`() {
        viewModel.getMovieList()
        coVerify(exactly = 1) { movieRepository.topRatedMovies() }
    }

    @Test
    fun `when getting movie list is successful, it should provide it`() = runBlocking {
        val movieList = mockk<List<Movie>>()
        coEvery { movieRepository.topRatedMovies() } returns movieList
        val observer = mockk<Observer<List<Movie>>>(relaxed = true)
        viewModel.movieListLiveData().observeForever(observer)
        viewModel.getMovieList()
        verify(exactly = 1) { observer.onChanged(movieList) }
    }

    @Test
    fun `when getting movie list, it should show and hide the loading bar`() = runBlocking {
        coEvery { movieRepository.topRatedMovies() } returns mockk()
        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.loadingLiveData().observeForever(observer)
        viewModel.getMovieList()
        verifySequence {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `when getting movie list is unsuccessful with a server error, it should show the error`() =
        runBlocking {
            val serverException = mockk<ServerException>()
            coEvery { movieRepository.topRatedMovies() } throws serverException
            val observer = mockk<Observer<ServerException>>(relaxed = true)
            viewModel.serverErrorLiveData().observeForever(observer)
            viewModel.getMovieList()
            verify(exactly = 1) { observer.onChanged(serverException) }
        }

    @Test
    fun `when getting movie list is unsuccessful with a network error, it should show the error`() {
        val message = "unknownHostException"
        val networkException = UnknownHostException(message)
        coEvery { movieRepository.topRatedMovies() } throws networkException
        val observer = mockk<Observer<NetworkException>>(relaxed = true)
        viewModel.networkErrorLiveData().observeForever(observer)
        viewModel.getMovieList()
        val slot = slot<NetworkException>()
        verify(exactly = 1) { observer.onChanged(capture(slot)) }
        TestCase.assertEquals(slot.captured.message, message)
    }
}