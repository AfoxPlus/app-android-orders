package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.network.response.BaseResponse
import com.afoxplus.orders.data.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.data.sources.network.api.response.OrderStatusResponse
import com.afoxplus.orders.data.sources.network.service.OrderNetworkService
import com.afoxplus.orders.cross.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class OrderNetworkServiceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockApi: OrderApiNetwork = mock()

    private val mockProperties: AppProperties = mock()

    private lateinit var sutService: OrderNetworkService

    @Before
    fun setup() {
        sutService = OrderNetworkService(mockApi, mockProperties)
    }

    @Test
    fun `GIVEN response WHEN getOrderStatus THEN is success`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: Response<BaseResponse<List<OrderStatusResponse>>> = mock()
            whenever(mockApi.getOrderStatus()).doReturn(mockResponse)

            //WHEN
            val result = sutService.getOrderStatus()

            //THEN
            verify(mockApi).getOrderStatus()
            Assert.assertNotNull(result)
        }
    }

    @Test(expected = Exception::class)
    fun `GIVEN exception WHEN getOrderStatus THEN throw exception`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            whenever(mockApi.getOrderStatus()).doThrow(Exception("ApiException"))

            //WHEN
            sutService.getOrderStatus()

            //THEN
            //Exception should be thrown
        }
    }

}