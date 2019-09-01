package com.kikyoung.movie.feature.list

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseFragment
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.util.extension.observeChanges
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.item_movie.*

class MovieDetailsFragment : BaseFragment<MovieViewModel>(
    MovieViewModel::class,
    R.layout.fragment_movie_details
) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActionBar()

        viewModel.selectedMovieLiveData().observeChanges(this) { movie ->
            showMovieDetails(movie)
        }
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
        titleTextView.text = movie.title
        overviewTextView.text = movie.overview
        Glide.with(this)
            .load(movie.posterUrl)
            .into(posterImageView)
    }
}