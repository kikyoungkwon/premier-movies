package com.kikyoung.movie.feature.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.kikyoung.movie.R
import com.kikyoung.movie.base.BaseScreenTest
import com.kikyoung.movie.feature.MainActivity
import com.kikyoung.movie.test.util.ViewVisibilityIdlingResource
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
        onView(ViewMatchers.withId(R.id.movieListRecyclerView)).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(viewVisibilityIdlingResource)
    }

    @Test
    fun showMovieList() {
        // TODO Test the poster image view with Bitmap sameAs method.
        testRecyclerViewItem(0, "title0", "overview0")
        testRecyclerViewItem(1, "title1", "overview1")
        testRecyclerViewItem(2, "title2", "overview2")
    }

    private fun testRecyclerViewItem(itemIndex: Int, title: String, overview: String) {
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.titleTextView))
            .check(matches(withText(title)))
        onView(withRecyclerView(R.id.movieListRecyclerView).atPositionOnView(itemIndex, R.id.overviewTextView))
            .check(matches(withText(overview)))
    }
}