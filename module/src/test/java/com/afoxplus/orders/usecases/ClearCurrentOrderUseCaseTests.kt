package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.ClearCurrentOrderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ClearCurrentOrderUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: ClearCurrentOrderUseCase by lazy {
        ClearCurrentOrderUseCase(
            mockRepository
        )
    }

    @Test
    fun `WHEN clearCurrentOrder THEN clear the order`() {
        testCoroutineRule.runBlockingTest {
            //WHEN
            val result = sutUseCase.invoke()

            //THEN
            verify(mockRepository).clearCurrentOrder()
            Assert.assertNotNull(result)
        }
    }
}