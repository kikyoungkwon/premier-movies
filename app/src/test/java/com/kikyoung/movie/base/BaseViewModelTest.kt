package com.kikyoung.movie.base

import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kikyoung.movie.data.exception.NetworkException
import com.kikyoung.movie.data.exception.ServerException
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.net.UnknownHostException
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class BaseViewModelTest : BaseKoinTest() {

    private val ioDispatcher = Dispatchers.Unconfined
    private lateinit var viewModel: BaseViewModel

    @Before
    fun before() {
        viewModel = BaseViewModel(ioDispatcher)
    }

    @Test
    fun `when a server error is occurred, it should provide it`() {
        val e = ServerException("message")
        val observer = mockk<Observer<ServerException>>(relaxed = true)
        viewModel.serverErrorLiveData().observeForever(observer)
        viewModel.handleRepositoryError(e)
        verify(exactly = 1) { observer.onChanged(e) }
    }

    @Test
    fun `when a network error is occurred, it should provide it`() {
        val message = "message"
        val e = UnknownHostException(message)
        val observer = mockk<Observer<NetworkException>>(relaxed = true)
        viewModel.networkErrorLiveData().observeForever(observer)
        viewModel.handleRepositoryError(e)
        val slot = slot<NetworkException>()
        verify(exactly = 1) { observer.onChanged(capture(slot)) }
        assertEquals(slot.captured.message, message)
    }

    @Test
    fun `when not network or server error is occurred, it should provide it`() {
        val message = "message"
        val e = IOException(message)
        val observer = mockk<Observer<ServerException>>(relaxed = true)
        viewModel.serverErrorLiveData().observeForever(observer)
        viewModel.handleRepositoryError(e)
        val slot = slot<ServerException>()
        verify(exactly = 1) { observer.onChanged(capture(slot)) }
        assertEquals(slot.captured.message, message)
    }
}