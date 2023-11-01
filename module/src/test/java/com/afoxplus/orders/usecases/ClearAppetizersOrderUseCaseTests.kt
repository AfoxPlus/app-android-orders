package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.ClearAppetizersOrderUseCase
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ClearAppetizersOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: ClearAppetizersOrderUseCase by lazy {
        ClearAppetizersOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product WHEN clearAppetizersOrder THEN clear the appetizers related to the product`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()

            //WHEN
            val result = sutUseCase.invoke(productMock)

            //THEN
            verify(mockRepository).clearAppetizersByProduct(productMock)
            Assert.assertNotNull(result)
        }
    }
}