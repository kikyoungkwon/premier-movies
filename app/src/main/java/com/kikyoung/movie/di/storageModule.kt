package com.kikyoung.movie.di

import android.content.Context
import com.kikyoung.movie.data.storage.MovieDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { movieDao(get()) }
    single { movieDatabase(androidContext()) }
}

private fun movieDao(database: MovieDatabase) = database.movieDao()
private fun movieDatabase(context: Context) = MovieDatabase.getDatabase(context)