package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.FetchAppetizerByOrderUseCase
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
class FetchAppetizerByOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: FetchAppetizerByOrderUseCase by lazy {
        FetchAppetizerByOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product WHEN findProductInOrder THEN return a list of order appetizer detail`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: List<OrderAppetizerDetail> = mock()
            val product: Product = mock()
            whenever(mockRepository.fetchAppetizersByProduct(product)).doReturn(mockResponse)

            //WHEN
            val result = sutUseCase.invoke(product)

            //THEN
            verify(mockRepository).fetchAppetizersByProduct(product)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
        }
    }
}