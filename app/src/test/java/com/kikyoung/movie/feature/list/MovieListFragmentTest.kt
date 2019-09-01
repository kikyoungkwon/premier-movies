package com.kikyoung.movie.feature.list

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseKoinTest
import com.kikyoung.movie.feature.list.model.Movie
import com.kikyoung.movie.test.util.withRecyclerView
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.mock.declare
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
class MovieListFragmentTest : BaseKoinTest() {

    private val movieListLiveData = MutableLiveData<List<Movie>>()
    private val movieViewModel = mockk<MovieViewModel>(relaxed = true)

    @Before
    fun before() {
        every { movieViewModel.movieListLiveData() } returns movieListLiveData
        declare { viewModel(override = true) { movieViewModel } }
        launchFragmentInContainer<MovieListFragment>()
    }

    @Test
    fun `when movie list is provided, it should show them`() {
        val movieList = movieList()
        movieListLiveData.postValue(movieList)
        onView(withId(R.id.movieListRecyclerView)).check(matches(isDisplayed()))
        // TODO Test the poster image view with Bitmap sameAs method.
        testRecyclerViewItem(0, movieList[0])
        testRecyclerViewItem(1, movieList[1])
        testRecyclerViewItem(2, movieList[2])
    }

    private fun movieList() = listOf(
        Movie("title0", "overview0", "http://localhost/post_path0.jpg"),
        Movie("title1", "overview1", "http://localhost/post_path1.jpg"),
        Movie("title2", "overview2", "http://localhost/post_path2.jpg")
    )

    private fun testRecyclerViewItem(itemIndex: Int, movie: Movie) {
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.titleTextView))
            .check(matches(withText(movie.title)))
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.overviewTextView))
            .check(matches(withText(movie.overview)))
    }
}