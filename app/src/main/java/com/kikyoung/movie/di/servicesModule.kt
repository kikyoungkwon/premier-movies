package com.kikyoung.movie.di

import com.kikyoung.movie.data.service.MovieService
import org.koin.dsl.module

val servicesModule = module {
    single { MovieService(get(), get()) }
}