package com.kikyoung.movie.di

import android.content.Context
import com.kikyoung.movie.BuildConfig
import com.kikyoung.movie.R
import com.kikyoung.movie.data.api.Api
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val NAME_BASE_URL = "baseUrl"
const val NAME_IMAGE_BASE_URL = "imageBaseUrl"

val networkModule = module {
    single { Moshi.Builder().build() }
    single { api(get()) }
    single { retrofit(get(named(NAME_BASE_URL)), get()) }
    single { okHttpClient(get()) }
    single { httpLoggingInterceptor() }
    single(named(NAME_BASE_URL)) { baseUrl(androidContext()) }
    single(named(NAME_IMAGE_BASE_URL)) { imageBaseUrl(androidContext()) }
}

private fun api(retrofit: Retrofit) = retrofit.create(Api::class.java)

private fun retrofit(baseUrl: String, okHttpClient: OkHttpClient) =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

private fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

private fun httpLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

private fun baseUrl(context: Context) = context.getString(R.string.base_url)

private fun imageBaseUrl(context: Context) = context.getString(R.string.image_base_url)