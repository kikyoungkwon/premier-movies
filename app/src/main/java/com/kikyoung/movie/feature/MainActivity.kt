package com.kikyoung.movie.feature

import android.os.Bundle
import androidx.navigation.findNavController
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseActivity
import com.kikyoung.movie.feature.list.MovieListFragmentDirections
import com.kikyoung.movie.feature.list.MovieViewModel
import com.kikyoung.movie.util.extension.hide
import com.kikyoung.movie.util.extension.observeChanges
import com.kikyoung.movie.util.extension.show
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieViewModel.showScreenLiveData().observeChanges(this) {
            if (it.first == MainScreen.DETAILS)
                findNavController(R.id.navHostFragment).navigate(
                    MovieListFragmentDirections.actionMovieListToMovieDetails(it.second as Int)
                )
        }

        movieViewModel.loadingLiveData().observeChanges(this) { visible ->
            // NOTE vs Data Binding
            progressBarViewGroup.apply { if (visible) show() else hide() }
        }
    }
}
