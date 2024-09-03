package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class AddOrUpdateProductToCurrentOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: AddOrUpdateProductToCurrentOrderUseCase by lazy {
        AddOrUpdateProductToCurrentOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product and quantity WHEN AddOrUpdateProductToCurrentOrder THEN add or update them to the order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2

            //WHEN
            val result = sutUseCase.invoke(quantityMock, productMock)

            //THEN
            verify(mockRepository).addOrUpdateProductToCurrentOrder(quantityMock, productMock, "")
            Assert.assertNotNull(result)
        }
    }
}