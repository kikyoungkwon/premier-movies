package com.kikyoung.movie.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kikyoung.movie.base.BaseViewModel
import com.kikyoung.movie.data.repository.MovieRepository
import com.kikyoung.movie.feature.MainScreen
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieRepository: MovieRepository,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private val showScreenLiveData = SingleLiveEvent<Pair<MainScreen, Any?>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val movieListLiveData = MutableLiveData<List<Movie>>()
    private val movieLiveData = MutableLiveData<Movie?>()

    fun getMovieList() {
        launch {
            try {
                loadingLiveData.postValue(true)
                movieListLiveData.postValue(movieRepository.getMovieList())
            } catch (e: Exception) {
                handleRepositoryError(e)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun showMovie(movie: Movie) {
        showScreenLiveData.postValue(Pair(MainScreen.DETAILS, movie.id))
    }

    fun getMovie(id: Int) {
        launch {
            try {
                loadingLiveData.postValue(true)
                movieLiveData.postValue(movieRepository.getMovie(id))
            } catch (e: Exception) {
                handleRepositoryError(e)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun showScreenLiveData(): LiveData<Pair<MainScreen, Any?>> = showScreenLiveData
    fun loadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun movieListLiveData(): LiveData<List<Movie>> = movieListLiveData
    fun movieLiveData(): LiveData<Movie?> = movieLiveData
}