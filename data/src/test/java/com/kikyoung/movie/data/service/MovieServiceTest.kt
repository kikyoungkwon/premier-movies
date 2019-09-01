package com.kikyoung.movie.data.service

import com.kikyoung.movie.data.api.Api
import com.kikyoung.movie.data.exception.ServerException
import com.kikyoung.movie.data.model.TopRatedResponse
import com.squareup.moshi.Moshi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MovieServiceTest {

    private var apiService: Api = mockk()
    private lateinit var movieService: MovieService

    @Before
    fun before() {
        movieService = MovieService(Moshi.Builder().build(), apiService)
    }

    @Test
    fun `when getting top rated movies is successful, it should return the result`() = runBlocking {
        val response = mockk<Response<TopRatedResponse>>()
        val responseBody = mockk<TopRatedResponse>()
        every { response.isSuccessful } returns true
        every { response.body() } returns responseBody
        coEvery { apiService.topRated(any()) } returns response
        assertEquals(movieService.topRated("api_key"), responseBody)
    }

    @Test(expected = ServerException::class)
    fun `when getting top rated movies is unsuccessful, it should throw an exception`() =
        runBlocking<Unit> {
            val response = mockk<Response<TopRatedResponse>>()
            every { response.isSuccessful } returns false
            every { response.code() } returns 500
            every {
                response.errorBody().toString()
            } returns "{\"status_code\":7,\"status_message\":\"Invalid API key: You must be granted a valid key.\"}"
            coEvery { apiService.topRated(any()) } returns response
            movieService.topRated("api_key")
        }
}