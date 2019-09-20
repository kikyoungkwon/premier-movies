package com.kikyoung.movie.feature.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseFragment
import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.feature.list.MovieViewModel
import com.kikyoung.movie.util.extension.observeChanges
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.view_movie_details.*

class MovieDetailsFragment : BaseFragment<MovieViewModel>(
    MovieViewModel::class,
    R.layout.fragment_movie_details
) {

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setActionBar()

        viewModel.movieLiveData().observeChanges(this) { movie ->
            if (movie == null) {
                // TODO If movie is null, show not found error message.
            } else showMovieDetails(movie)
        }

        viewModel.getMovie(args.id)
    }

    private fun setActionBar() {
        try {
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).apply {
                setSupportActionBar(movieDetailsToolbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setTitle(R.string.movie_details_title)
                }
            }
        } catch (e: ClassCastException) {
            // Ignore for FragmentScenario tests
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMovieDetails(movie: Movie) {
        movieTitleTextView.text = movie.title
        movieOverviewTextView.text = movie.overview
        Glide.with(this)
            .load(movie.posterUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(moviePosterImageView)
    }
}