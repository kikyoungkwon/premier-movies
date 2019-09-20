package com.kikyoung.movie.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kikyoung.movie.data.model.Movie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<Movie>)

    @Query("SELECT * FROM movie")
    suspend fun loadMovies(): List<Movie>
}