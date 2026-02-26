package com.pierreduchemin.smsforward.utils

import android.content.Context
import com.pierreduchemin.smsforward.data.ForwardModelRepository
import com.pierreduchemin.smsforward.data.GlobalModelRepository
import com.pierreduchemin.smsforward.data.source.database.GlobalModel
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RedirectionManagerTest {

    private lateinit var redirectionManager: RedirectionManager
    private val globalModelRepository: GlobalModelRepository = mock()
    private val forwardModelRepository: ForwardModelRepository = mock()
    private val context: Context = mock()

    @Before
    fun setUp() {
        redirectionManager = RedirectionManager(globalModelRepository, forwardModelRepository)
    }

    @Test
    fun `onSmsReceived should not forward if blacklist matches word`() {
        val globalModel = GlobalModel(1, activated = true, blacklist = "spam, promo")
        whenever(globalModelRepository.getGlobalModel()).thenReturn(globalModel)

        redirectionManager.onSmsReceived(context, "123456", "This is a promo message")

        verify(forwardModelRepository, never()).getForwardModels()
    }

    @Test
    fun `onSmsReceived should not forward if blacklist matches regex`() {
        val globalModel = GlobalModel(1, activated = true, blacklist = "sp.m, [0-9]{3}")
        whenever(globalModelRepository.getGlobalModel()).thenReturn(globalModel)

        redirectionManager.onSmsReceived(context, "123456", "Your code is 123")

        verify(forwardModelRepository, never()).getForwardModels()
    }

    @Test
    fun `onSmsReceived should forward if blacklist does not match`() {
        val globalModel = GlobalModel(1, activated = true, blacklist = "spam, promo")
        whenever(globalModelRepository.getGlobalModel()).thenReturn(globalModel)
        whenever(forwardModelRepository.getForwardModels()).thenReturn(emptyList())

        redirectionManager.onSmsReceived(context, "123456", "Hello world")

        verify(forwardModelRepository, times(1)).getForwardModels()
    }

    @Test
    fun `onSmsReceived should be case-insensitive`() {
        val globalModel = GlobalModel(1, activated = true, blacklist = "SPAM")
        whenever(globalModelRepository.getGlobalModel()).thenReturn(globalModel)

        redirectionManager.onSmsReceived(context, "123456", "this is some spam")

        verify(forwardModelRepository, never()).getForwardModels()
    }

    @Test
    fun `onSmsReceived should handle blacklist with empty entries`() {
        val globalModel = GlobalModel(1, activated = true, blacklist = "spam, , , promo")
        whenever(globalModelRepository.getGlobalModel()).thenReturn(globalModel)
        whenever(forwardModelRepository.getForwardModels()).thenReturn(emptyList())

        redirectionManager.onSmsReceived(context, "123", "hello")

        verify(forwardModelRepository, times(1)).getForwardModels()
    }
}
