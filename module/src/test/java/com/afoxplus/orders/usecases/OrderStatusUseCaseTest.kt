package com.afoxplus.orders.usecases

import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.orders.domain.repositories.OrderStatusRepository
import com.afoxplus.orders.domain.usecases.OrderStatusUseCase
import com.afoxplus.orders.cross.TestCoroutineRule
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
            val result = sutUseCase.invoke()

            //THEN
            verify(mockRepository).getOrderStatus()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
        }
    }

}