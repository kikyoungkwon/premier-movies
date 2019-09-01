package com.kikyoung.movie.di

import com.kikyoung.movie.feature.list.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get(), get(named(NAME_UI))) }
}