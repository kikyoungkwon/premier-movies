package com.kikyoung.movie.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kikyoung.movie.data.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        private const val NAME = "movie_database"

        fun getDatabase(context: Context): MovieDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}