package com.kikyoung.movie.feature

import android.os.Bundle
import androidx.navigation.findNavController
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseActivity
import com.kikyoung.movie.feature.list.MovieDetailsFragment.Companion.ARG_ID
import com.kikyoung.movie.feature.list.MovieViewModel
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.util.extension.hide
import com.kikyoung.movie.util.extension.observeChanges
import com.kikyoung.movie.util.extension.show
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * To run with deep link on command line:
 * adb shell am start -W -a android.intent.action.VIEW -d "http://www.kikyoung.com/movies/19404" com.kikyoung.movie.debug
 */
class MainActivity : BaseActivity() {

    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieViewModel.showScreenLiveData().observeChanges(this) { pair ->
            when(pair.first) {
                MainScreen.DETAILS -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_movieList_to_movieDetails, Bundle().apply {
                        putString(ARG_ID, (pair.second as Movie).id)
                    })
                }
            }
        }

        movieViewModel.loadingLiveData().observeChanges(this) { visible ->
            // NOTE vs Data Binding
            progressBarViewGroup.apply { if (visible) show() else hide() }
        }
    }
}
