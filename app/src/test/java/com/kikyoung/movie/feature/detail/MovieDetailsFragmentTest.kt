package com.kikyoung.movie.feature.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseKoinTest
import com.kikyoung.movie.data.model.Movie
import com.kikyoung.movie.feature.list.MovieViewModel
import com.kikyoung.movie.util.TestUtil
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.mock.declare
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
class MovieDetailsFragmentTest : BaseKoinTest() {

    private val movieLiveData = MutableLiveData<Movie>()
    private val movieViewModel = mockk<MovieViewModel>(relaxed = true)

    @Test
    fun `when a movie is provided, it should show it`() {
        every { movieViewModel.movieLiveData() } returns movieLiveData
        declare { viewModel(override = true) { movieViewModel } }
        val args = Bundle().apply { putInt("id", 0) }
        launchFragmentInContainer<MovieDetailsFragment>(args)
        val movie = TestUtil.createMovie(0)
        movieLiveData.postValue(movie)
        onView(withId(R.id.movieTitleTextView)).check(matches(withText(movie.title)))
        onView(withId(R.id.movieOverviewTextView)).check(matches(withText(movie.overview)))
    }
}