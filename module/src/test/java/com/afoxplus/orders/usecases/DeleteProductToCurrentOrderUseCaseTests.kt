package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class DeleteProductToCurrentOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: DeleteProductToCurrentOrderUseCase by lazy {
        DeleteProductToCurrentOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product WHEN deleteProductToCurrentOrder THEN delete it from the order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val product: Product = mock()

            //WHEN
            val result = sutUseCase.invoke(product)

            //THEN
            verify(mockRepository).deleteProductToCurrentOrder(product)
            Assert.assertNotNull(result)
        }
    }
}