package com.kikyoung.movie.data.storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kikyoung.movie.data.util.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {

    private lateinit var moviesDao: MoviesDao
    private lateinit var db: MovieDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        moviesDao = db.movieDao()
    }

    @Test
    fun `when after saving movie list, it should return the same`() = runBlocking {
        val movieList = TestUtil.createMovies(2)
        moviesDao.saveMovies(movieList)
        val savedMovieList = moviesDao.loadMovies()
        assertEquals(movieList[0].id, savedMovieList[0].id)
        assertEquals(movieList[1].id, savedMovieList[1].id)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}