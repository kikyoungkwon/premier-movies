package com.kikyoung.movie.feature

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseKoinTest
import com.kikyoung.movie.feature.list.MovieViewModel
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.util.SingleLiveEvent
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseKoinTest() {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)

    private val showScreenLiveData = SingleLiveEvent<Pair<MainScreen, Any?>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val movieLiveData = MutableLiveData<Movie?>()

    @Before
    fun before() {
        val movieViewModel = mockk<MovieViewModel>(relaxed = true)
        every { movieViewModel.loadingLiveData() } returns loadingLiveData
        every { movieViewModel.showScreenLiveData() } returns showScreenLiveData
        every { movieViewModel.movieLiveData() } returns movieLiveData
        declare { viewModel(override = true) { movieViewModel } }
        activityRule.launchActivity(null)
    }

    @Test
    fun `when it needs to show loading, it should show a loading bar`() {
        loadingLiveData.postValue(true)
        onView(withId(R.id.progressBarViewGroup)).check(matches(isDisplayed()))
    }

    @Test
    fun `when it needs to hide loading, it should hide a loading bar`() {
        loadingLiveData.postValue(false)
        onView(withId(R.id.progressBarViewGroup)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `when it needs to show movie details, it should show it`() {
        val movie = Movie(0, "title0", "overview0", "http://localhost/post_path0.jpg")
        movieLiveData.postValue(movie)
        showScreenLiveData.postValue(Pair(MainScreen.DETAILS, 0))
        onView(withId(R.id.movieTitleTextView)).check(matches(withText(movie.title)))
    }
}