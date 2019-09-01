package com.kikyoung.movie.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import timber.log.Timber
import java.util.*
import java.util.concurrent.CountDownLatch

class TestMockWebServer {

    private val mockWebServer = MockWebServer()
    private val testDataLoader = TestDataLoader()
    private val dispatchRoutes = HashMap<String, MockResponse>()
    private var baseUrl: String? = null

    init {
        startServer()
        setDispatcher()
    }

    private fun startServer() {
        val serverLatch = CountDownLatch(1)

        Thread {
            try {
                mockWebServer.start()
                baseUrl = mockWebServer.url("").toUrl().toString()
                serverLatch.countDown()
            } catch (t: Throwable) {
                Timber.e(t, "Error starting server")
            }
        }.start()

        try {
            serverLatch.await()
        } catch (e: InterruptedException) {
            Timber.e(e, "Error waiting for server to start")
        }
    }

    private fun setDispatcher() {
        mockWebServer.dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.requestUrl?.encodedPath.toString()
                return dispatchRoutes.asSequence().firstOrNull {
                    // TODO Use Regex
                    path.startsWith(it.key)
                }?.value ?: throw IllegalStateException("$path was not enqueued")
            }
        }
    }

    fun getBaseUrl(): String? = baseUrl

    fun setTopRatedMoviesResponse(filePath: String) {
        enqueueSuccess("/3/movie/top_rated", testDataLoader.loadString("topRated/$filePath"))
    }

    private fun enqueueSuccess(urlPath: String, body: String) {
        dispatchRoutes[urlPath] = MockResponse().setResponseCode(200).setBody(body)
    }
}