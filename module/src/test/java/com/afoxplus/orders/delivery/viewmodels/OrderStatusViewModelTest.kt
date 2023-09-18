package com.afoxplus.orders.delivery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.afoxplus.orders.delivery.models.OrderStateUIModel
import com.afoxplus.orders.domain.usecases.OrderStatusUseCase
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.UIKitCoroutinesDispatcherTest
import com.afoxplus.orders.cross.fakeOrderStatus
import com.afoxplus.orders.cross.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderStatusViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockUseCase: OrderStatusUseCase = mock()

    private val mockDispatcher: UIKitCoroutinesDispatcherTest by lazy { UIKitCoroutinesDispatcherTest() }

    private lateinit var sutViewModel: OrderStatusViewModel

    @Before
    fun setup() {
        sutViewModel = OrderStatusViewModel(mockUseCase, mockDispatcher)
    }

    @Test
    fun `GIVEN list OrderStats WHEN fetchStateOrders THEN return Success`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockData = arrayListOf(fakeOrderStatus)
            whenever(mockUseCase.invoke()).doReturn(mockData)

            //WHEN
            sutViewModel.fetchStateOrders()
            val data = sutViewModel.orders.getOrAwaitValue()

            //THEN
            assert(data is OrderStateUIModel.Success)
            assertEquals(1, (data as OrderStateUIModel.Success).orders.size)
            verify(mockUseCase).invoke()
        }

    }

    @Test
    fun `GIVEN emptylist WHEN fetchStateOrders THEN return Success`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            whenever(mockUseCase.invoke()).doReturn(emptyList())

            //WHEN
            sutViewModel.fetchStateOrders()
            val result = sutViewModel.orders.getOrAwaitValue()

            //THEN
            assert(result is OrderStateUIModel.Success)
            verify(mockUseCase).invoke()
        }
    }


    @Test
    fun `GIVEN Exception WHEN fetchStateOrders THEN return Error`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val exception = Exception("ApiException")
            whenever(mockUseCase.invoke()).doAnswer { throw exception }

            //WHEN
            sutViewModel.fetchStateOrders()
            val result = sutViewModel.orders.getOrAwaitValue()

            //THEN
            assert(result is OrderStateUIModel.Error)
            assertEquals(exception, (result as OrderStateUIModel.Error).error)
            verify(mockUseCase).invoke()
        }
    }

}