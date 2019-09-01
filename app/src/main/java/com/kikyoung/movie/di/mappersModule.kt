package com.kikyoung.movie.di

import com.kikyoung.movie.data.mapper.MovieMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mappersModule = module {
    single { MovieMapper(get(named(NAME_IMAGE_BASE_URL))) }
}