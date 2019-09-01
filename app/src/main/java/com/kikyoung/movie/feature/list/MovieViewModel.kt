package com.kikyoung.movie.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kikyoung.movie.base.BaseViewModel
import com.kikyoung.movie.data.repository.MovieRepository
import com.kikyoung.movie.feature.list.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieRepository: MovieRepository,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val movieListLiveData = MutableLiveData<List<Movie>>()

    fun getMovieList() {
        launch {
            try {
                loadingLiveData.postValue(true)
                movieListLiveData.postValue(movieRepository.topRatedMovies())
            } catch (e: Exception) {
                handleRepositoryError(e)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun loadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun movieListLiveData(): LiveData<List<Movie>> = movieListLiveData
}