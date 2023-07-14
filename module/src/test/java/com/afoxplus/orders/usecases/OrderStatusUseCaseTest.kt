package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.usecases.repositories.OrderStatusRepository
import com.afoxplus.orders.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderStatusUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderStatusRepository = mock()

    private val sutUseCase: OrderStatusUseCase by lazy { OrderStatusUseCase(mockRepository) }

    @Test
    fun `GIVEN list OrderStatus WHEN getStatusOrders THEN return listOrderStatus`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: List<OrderStatus> = mock()
            whenever(mockRepository.getOrderStatus()).doReturn(mockResponse)

            //WHEN
            val result = sutUseCase.getStatusOrders()

            //THEN
            verify(mockRepository).getOrderStatus()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
        }
    }

}