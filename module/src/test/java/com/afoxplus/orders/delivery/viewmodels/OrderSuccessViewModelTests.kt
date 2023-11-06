package com.afoxplus.orders.delivery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.UIKitCoroutinesDispatcherTest
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.uikit.bus.UIKitEventBus
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderSuccessViewModelTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockEventBusWrapper: UIKitEventBusWrapper = mock()
    private val mockDispatcher: UIKitCoroutineDispatcher by lazy { UIKitCoroutinesDispatcherTest() }

    private val sutViewModel: OrderSuccessViewModel by lazy {
        OrderSuccessViewModel(
            mockEventBusWrapper,
            mockDispatcher
        )
    }

    @Test
    fun `WHEN clickOnNewOrder THEN call the new order event`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockShareFlow: SharedFlow<UIKitEventBus> = MutableSharedFlow()
            whenever(mockEventBusWrapper.listen()).thenReturn(mockShareFlow)
            //WHEN
            sutViewModel.clickOnNewOrder()

            //THEN
            sutViewModel.viewModelScope.launch {
                sutViewModel.onEventBusListener.collect {
                    Assert.assertNotNull(it)
                }
            }
        }
    }

    @Test
    fun `WHEN clickGoToHome THEN call go home event`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockShareFlow: SharedFlow<UIKitEventBus> = MutableSharedFlow()
            whenever(mockEventBusWrapper.listen()).thenReturn(mockShareFlow)
            //WHEN
            sutViewModel.clickGoToHome()

            //THEN
            sutViewModel.viewModelScope.launch {
                sutViewModel.onEventBusListener.collect {
                    Assert.assertNotNull(it)
                }
            }
        }
    }
}