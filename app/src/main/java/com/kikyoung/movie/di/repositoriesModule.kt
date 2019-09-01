package com.kikyoung.movie.di

import com.kikyoung.movie.data.repository.MovieRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoriesModule = module {
    single { MovieRepository(get(), get(), get(named(NAME_IO))) }
}