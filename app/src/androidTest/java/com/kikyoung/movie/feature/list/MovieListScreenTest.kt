package com.kikyoung.movie.feature.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseScreenTest
import com.kikyoung.movie.feature.MainActivity
import com.kikyoung.movie.test.util.ViewVisibilityIdlingResource
import com.kikyoung.movie.test.util.actionOnItemViewAtPosition
import com.kikyoung.movie.test.util.withRecyclerView
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// NOTE vs BDD, vs Robot pattern
class MovieListScreenTest : BaseScreenTest() {

    companion object {
        private const val RESPONSE_FILE_LATEST_SUCCESS = "success.json"
    }

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    override fun before() {
        super.before()
        mockWebServer.setTopRatedMoviesResponse(RESPONSE_FILE_LATEST_SUCCESS)
        activityRule.launchActivity(null)
        val viewVisibilityIdlingResource = ViewVisibilityIdlingResource(
            activityRule.activity.findViewById<RecyclerView>(R.id.movieListRecyclerView),
            View.VISIBLE
        )
        IdlingRegistry.getInstance().register(viewVisibilityIdlingResource)
        onView(withId(R.id.movieListRecyclerView)).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(viewVisibilityIdlingResource)
    }

    @Test
    fun showMovieList() {
        testMovieListItem(0, "title0", "overview0")
        testMovieListItem(1, "title1", "overview1")
        testMovieListItem(2, "title2", "overview2")
    }

    @Test
    fun showMovieDetails() {
        onView(withId(R.id.movieListRecyclerView)).perform(
            actionOnItemViewAtPosition(1, R.id.itemOverviewTextView, click())
        )
        onView(withId(R.id.movieTitleTextView)).check(matches(withText("title1")))
        onView(withId(R.id.movieOverviewTextView)).check(matches(withText("overview1")))
    }

    private fun testMovieListItem(itemIndex: Int, title: String, overview: String) {
        // TODO Test the poster image view with Bitmap sameAs method.
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.itemTitleTextView))
            .check(matches(withText(title)))
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.itemOverviewTextView))
            .check(matches(withText(overview)))
    }
}