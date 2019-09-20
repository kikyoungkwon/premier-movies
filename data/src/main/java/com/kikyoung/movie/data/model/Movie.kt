package com.kikyoung.movie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String
)