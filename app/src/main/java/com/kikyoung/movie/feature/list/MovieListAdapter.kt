package com.kikyoung.movie.feature.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikyoung.movie.R
import com.kikyoung.movie.feature.list.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*
import java.util.concurrent.locks.ReentrantLock

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private val movieList = mutableListOf<Movie>()
    private var itemClickListener: ItemClickListener? = null
    private val lock = ReentrantLock()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) =
            view.apply {
                titleTextView.text = movie.title
                overviewTextView.text = movie.overview
                Glide.with(this)
                    .load(movie.posterUrl)
                    .into(posterImageView)
                setOnClickListener {
                    itemClickListener?.onItemClick(movie)
                }
            }
    }

    fun updateMovieList(newMovies: List<Movie>) {
        lock.lock()
        try {
            movieList.clear()
            movieList.addAll(newMovies)
            notifyDataSetChanged()
        } finally {
            lock.unlock()
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    interface ItemClickListener {
        fun onItemClick(movie: Movie)
    }
}