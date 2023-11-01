package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.FindProductInOrderUseCase
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class FindProductInOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: FindProductInOrderUseCase by lazy {
        FindProductInOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product WHEN findProductInOrder THEN return a orderDetail`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: OrderDetail = mock()
            val product: Product = mock()
            whenever(mockRepository.findProductInCurrentOrder(product)).doReturn(mockResponse)

            //WHEN
            val result = sutUseCase.invoke(product)

            //THEN
            verify(mockRepository).findProductInCurrentOrder(product)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
        }
    }
}