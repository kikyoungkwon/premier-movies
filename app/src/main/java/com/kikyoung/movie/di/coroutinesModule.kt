package com.kikyoung.movie.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val NAME_UI = "ui"
const val NAME_IO = "io"

val coroutinesModule = module {
    single<CoroutineDispatcher>(named(NAME_UI)) { Dispatchers.Main }
    single(named(NAME_IO)) { Dispatchers.IO }
}