package com.kikyoung.movie.feature.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseFragment
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.util.extension.observeChanges
import com.kikyoung.movie.util.extension.show
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : BaseFragment<MovieViewModel>(
    MovieViewModel::class,
    R.layout.fragment_movie_list
) {

    private val movieListAdapter = MovieListAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActionBar()

        movieListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieListAdapter
        }

        movieListAdapter.setItemClickListener(object: MovieListAdapter.ItemClickListener {
            override fun onItemClick(movie: Movie) {
                viewModel.showMovie(movie)
            }
        })

        viewModel.movieListLiveData().observeChanges(this) { movies ->
            showMovieList(movies)
        }

        viewModel.getMovieList()
    }

    private fun setActionBar() {
        try {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(movieListToolbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(false)
                    setTitle(R.string.movie_list_title)
                }
            }
        } catch (e: ClassCastException) {
            // Ignore for FragmentScenario tests
        }
    }

    private fun showMovieList(movies: List<Movie>) {
        movieListAdapter.updateMovieList(movies)
        movieListRecyclerView.show()
    }

    override fun onDestroyView() {
        // For preventing memory leak on adapter
        movieListRecyclerView.adapter = null
        super.onDestroyView()
    }
}