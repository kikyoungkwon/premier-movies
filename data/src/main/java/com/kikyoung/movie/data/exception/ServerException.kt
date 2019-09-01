package com.kikyoung.movie.data.exception

import com.squareup.moshi.Json
import java.io.IOException

data class ServerException(
    @field:Json(name = "status_message")
    val statusMessage: String?,
    @field:Json(name = "status_code")
    val statusCode: Int = 0
) : IOException(statusMessage)