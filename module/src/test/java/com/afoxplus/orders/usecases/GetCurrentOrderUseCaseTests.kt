package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.GetCurrentOrderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetCurrentOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: GetCurrentOrderUseCase by lazy { GetCurrentOrderUseCase(mockRepository) }

    @Test
    fun `WHEN getCurrentOrder THEN return a shareflow of the order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: SharedFlow<Order> = mock()
            whenever(mockRepository.getCurrentOrder()).doReturn(mockResponse)

            //WHEN
            val result = sutUseCase.invoke()

            //THEN
            verify(mockRepository).getCurrentOrder()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
            Assert.assertEquals(result.collect(), mockResponse.collect())
        }
    }
}