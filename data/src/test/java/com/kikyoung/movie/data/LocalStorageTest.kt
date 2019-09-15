package com.kikyoung.movie.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kikyoung.movie.base.BaseKoinTest
import com.kikyoung.movie.data.repository.MovieRepository.Companion.KEY_MOVIE_LIST
import com.kikyoung.movie.feature.list.model.Movie
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalStorageTest : BaseKoinTest() {

    private val key = KEY_MOVIE_LIST
    private val localStorage: LocalStorage =
        LocalStorage(InstrumentationRegistry.getInstrumentation().context, Moshi.Builder().build())
    private val type = Types.newParameterizedType(List::class.java, Movie::class.java)

    @Test
    fun `when after saving movie list, it should return a not null value`() {
        val savedMovieList = getMovieList()
        localStorage.put(key, type, savedMovieList)
        val movieList = localStorage.get<List<Movie>>(key, type)
        assertEquals(savedMovieList.size, movieList?.size)
        assertEquals(savedMovieList[0].id, movieList?.get(0)?.id)
        assertEquals(savedMovieList[1].id, movieList?.get(1)?.id)
    }

    @Test
    fun `when after clearing movie list, it should return a null value`() {
        val savedMovieList = getMovieList()
        localStorage.put(key, type, savedMovieList)
        localStorage.put(key, type, null)
        assertNull(localStorage.get(key, type))
    }

    @Test
    fun `when a key does not exist, it should return null`() {
        assertNull(localStorage.get("${System.currentTimeMillis()}", type))
    }

    @Test
    fun `when the data model is changed, it should return null`() {
        localStorage.put(key, type, getMovieList())
        assertNull(localStorage.get(key, Int::class.java))
    }

    private fun getMovieList(): List<Movie> {
        return listOf(
            Movie(0, "title0", "overview0", "posterUrl0"),
            Movie(1, "title1", "overview1", "posterUrl1")
        )
    }
}