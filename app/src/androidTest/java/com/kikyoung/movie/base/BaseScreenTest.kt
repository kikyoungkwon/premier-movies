package com.kikyoung.movie.base

import android.os.AsyncTask
import androidx.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kikyoung.movie.data.TestMockWebServer
import com.kikyoung.movie.di.NAME_BASE_URL
import com.kikyoung.movie.di.NAME_IO
import kotlinx.coroutines.asCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declare

open class BaseScreenTest : KoinTest {

    private val okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp3", get())
    protected val mockWebServer = TestMockWebServer()

    @Before
    open fun before() {
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        declare {
            single(named(NAME_IO), override = true) {
                AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
            }
            single(named(NAME_BASE_URL), override = true) {
                mockWebServer.getBaseUrl()
            }
        }
    }

    @After
    open fun after() {
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }
}