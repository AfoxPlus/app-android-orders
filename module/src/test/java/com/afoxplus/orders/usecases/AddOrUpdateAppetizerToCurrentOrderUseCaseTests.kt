package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.AddOrUpdateAppetizerToCurrentOrderUseCase
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class AddOrUpdateAppetizerToCurrentOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: AddOrUpdateAppetizerToCurrentOrderUseCase by lazy {
        AddOrUpdateAppetizerToCurrentOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `GIVEN a product, an appetizer and the quantity WHEN addOrUpdateAppetizerToCurrentOrder THEN add or update them to the order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val appetizerMock: Product = mock()
            val quantityMock = 2

            //WHEN
            val result = sutUseCase.invoke(quantityMock, appetizerMock, productMock)

            //THEN
            verify(mockRepository).addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )
            Assert.assertNotNull(result)
        }
    }
}