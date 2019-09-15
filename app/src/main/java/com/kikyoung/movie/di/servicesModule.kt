package com.kikyoung.movie.di

import com.kikyoung.movie.data.service.MovieService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val servicesModule = module {
    single { MovieService(get(named(NAME_API_KEY)), get(), get()) }
}