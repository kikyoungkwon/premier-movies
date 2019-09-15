package com.kikyoung.movie.di

import com.kikyoung.movie.data.LocalStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { LocalStorage(androidContext(), get()) }
}