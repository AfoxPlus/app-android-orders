package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.exceptions.ApiErrorException
import com.afoxplus.orders.cross.exceptions.ExceptionMessage
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.usecases.SendOrderUseCase
import com.afoxplus.uikit.common.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SendOrderUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: OrderRepository = mock()
    private val sutUseCase: SendOrderUseCase by lazy { SendOrderUseCase(mockRepository) }

    @Test
    fun `GIVEN an Order WHEN sendOrder THEN return successful message`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val order: Order = mock()
            val mockSuccessMessage = "Order successful"
            val mockResponse: ResultState<String> = ResultState.Success(mockSuccessMessage)
            whenever(sutUseCase.invoke(order)).doReturn(mockResponse)

            //WHEN
            val result:ResultState<String> = sutUseCase.invoke(order)

            //THEN
            verify(mockRepository).sendOrder(order)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
            Assert.assertTrue(result is ResultState.Success)
            Assert.assertTrue((result as ResultState.Success).data == mockSuccessMessage)
        }
    }

    @Test
    fun `GIVEN an Order WHEN sendOrder THEN return an error exception`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val order: Order = mock()
            val mockErrorException = ApiErrorException(
                contentMessage = ExceptionMessage(
                    value = "No se ha podido enviar el pedido",
                    info = "¿Quieres intentarlo nuevamente?"
                )
            )
            val mockResponse: ResultState<String> = ResultState.Error(mockErrorException)
            whenever(sutUseCase.invoke(order)).doReturn(mockResponse)

            //WHEN
            val result:ResultState<String> = sutUseCase.invoke(order)

            //THEN
            verify(mockRepository).sendOrder(order)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockResponse)
            Assert.assertTrue(result is ResultState.Error)
            Assert.assertTrue((result as ResultState.Error).exception.message == mockErrorException.message)
        }
    }
}