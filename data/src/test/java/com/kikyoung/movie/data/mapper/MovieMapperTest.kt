package com.kikyoung.movie.data.mapper

import com.kikyoung.movie.data.model.MovieResponse
import com.kikyoung.movie.data.model.TopRatedResponse
import com.kikyoung.movie.feature.list.model.Movie
import org.junit.Test
import kotlin.test.assertEquals

class MovieMapperTest {

    companion object {
        private const val IMAGE_BASE_URL = "http://image.base.url"
    }

    private var mapper = MovieMapper(IMAGE_BASE_URL)

    @Test
    fun `when mapping movies response to list, it should map correctly`() {
        val moviesResponse = moviesResponse()
        val movieList = mapper.toMovieList(moviesResponse)
        assertMovie(moviesResponse.movies[0], movieList[0])
        assertMovie(moviesResponse.movies[1], movieList[1])
        assertMovie(moviesResponse.movies[2], movieList[2])
    }

    private fun assertMovie(movieResponse: MovieResponse, movie: Movie) {
        assertEquals(movieResponse.title, movie.title)
        assertEquals(movieResponse.overview, movie.overview)
        assertEquals("${IMAGE_BASE_URL}${movieResponse.posterFilePath}", movie.posterUrl)
    }

    private fun moviesResponse() = TopRatedResponse(
        listOf(
            MovieResponse("title0", "overview0", "porterFilePath0.jpg"),
            MovieResponse("title1", "overview1", "porterFilePath1.jpg"),
            MovieResponse("title2", "overview2", "porterFilePath2.jpg")
        )
    )
}