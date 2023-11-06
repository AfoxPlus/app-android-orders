package com.afoxplus.orders.data.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.network.response.BaseResponse
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.exceptions.ApiErrorException
import com.afoxplus.orders.cross.exceptions.OrderBusinessException
import com.afoxplus.orders.data.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.data.sources.network.api.request.OrderRequest
import com.afoxplus.orders.data.sources.network.api.response.ClientStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderBaseResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderMessageResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderTypeStatusResponse
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderType
import com.afoxplus.uikit.common.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class OrderNetworkServiceTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockOrderApiNetwork: OrderApiNetwork = mock()
    private val mockAppProperties: AppProperties = mock()

    private val sutNetwork: OrderNetworkService by lazy {
        OrderNetworkService(
            mockOrderApiNetwork,
            mockAppProperties
        )
    }

    @Test
    fun `GIVEN an order WHEN sendOrder THEN return a ResultState with a successful message`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = createOrder()
            val mockResponse: Response<OrderBaseResponse<OrderResponse>> =
                createApiSuccessfulResponse()
            val orderRequest: OrderRequest = OrderRequest.getOrderRequest(orderMock)

            val mockHeader = mapOf(OrderNetworkService.API_HEADERS_CURRENCY_ID to "currencyId")
            whenever(mockOrderApiNetwork.sendOrder(mockHeader, orderRequest)).doReturn(mockResponse)
            whenever(mockAppProperties.getCurrencyID()).doReturn("currencyId")

            //WHEN
            val result = sutNetwork.sendOrder(orderMock)

            //THEN
            verify(mockOrderApiNetwork).sendOrder(mockHeader, orderRequest)
            verify(mockAppProperties).getCurrencyID()
            Assert.assertNotNull(result)
            Assert.assertTrue(result is ResultState.Success)
            Assert.assertEquals((result as ResultState.Success).data, "Katelan")
        }
    }

    @Test
    fun `GIVEN an order WHEN sendOrder and the api is successful but the body success is false THEN return a ResultState error OrderBusinessException`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = createOrder()
            val mockResponse: Response<OrderBaseResponse<OrderResponse>> =
                createApiSuccessfulBodyFalseResponse()
            val orderRequest: OrderRequest = OrderRequest.getOrderRequest(orderMock)

            val mockHeader = mapOf(OrderNetworkService.API_HEADERS_CURRENCY_ID to "currencyId")
            whenever(mockOrderApiNetwork.sendOrder(mockHeader, orderRequest)).doReturn(mockResponse)
            whenever(mockAppProperties.getCurrencyID()).doReturn("currencyId")

            //WHEN
            val result = sutNetwork.sendOrder(orderMock)

            //THEN
            verify(mockOrderApiNetwork).sendOrder(mockHeader, orderRequest)
            verify(mockAppProperties).getCurrencyID()
            Assert.assertNotNull(result)
            Assert.assertTrue(result is ResultState.Error)
            Assert.assertTrue((result as ResultState.Error).exception is OrderBusinessException)
            Assert.assertEquals(
                (result.exception as OrderBusinessException).contentMessage.value,
                "An internal error occur"
            )
            Assert.assertEquals(
                (result.exception as OrderBusinessException).contentMessage.info,
                "be careful with this issue"
            )
        }
    }

    @Test
    fun `GIVEN an order WHEN sendOrder and the api returns unsuccessful THEN return a ResultState error ApiErrorException`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = createOrder()
            val mockResponse: Response<OrderBaseResponse<OrderResponse>> =
                createApiUnsuccessfulResponse()
            val orderRequest: OrderRequest = OrderRequest.getOrderRequest(orderMock)

            val mockHeader = mapOf(OrderNetworkService.API_HEADERS_CURRENCY_ID to "currencyId")
            whenever(mockOrderApiNetwork.sendOrder(mockHeader, orderRequest)).doReturn(mockResponse)
            whenever(mockAppProperties.getCurrencyID()).doReturn("currencyId")

            //WHEN
            val result = sutNetwork.sendOrder(orderMock)

            //THEN
            verify(mockOrderApiNetwork).sendOrder(mockHeader, orderRequest)
            verify(mockAppProperties).getCurrencyID()
            Assert.assertNotNull(result)
            Assert.assertTrue(result is ResultState.Error)
            Assert.assertTrue((result as ResultState.Error).exception is ApiErrorException)
            Assert.assertEquals(
                (result.exception as ApiErrorException).contentMessage.value,
                "No se ha podido enviar el pedido"
            )
            Assert.assertEquals(
                (result.exception as ApiErrorException).contentMessage.info,
                "¿Quieres intentarlo nuevamente?"
            )
        }
    }

    @Test
    fun `GIVEN an order WHEN sendOrder and the api returns success response but with an empty body THEN return a ResultState error ApiErrorException`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = createOrder()
            val mockResponse: Response<OrderBaseResponse<OrderResponse>> =
                createApiUnsuccessfulBodyNullResponse()
            val orderRequest: OrderRequest = OrderRequest.getOrderRequest(orderMock)

            val mockHeader = mapOf(OrderNetworkService.API_HEADERS_CURRENCY_ID to "currencyId")
            whenever(mockOrderApiNetwork.sendOrder(mockHeader, orderRequest)).doReturn(mockResponse)
            whenever(mockAppProperties.getCurrencyID()).doReturn("currencyId")

            //WHEN
            val result = sutNetwork.sendOrder(orderMock)

            //THEN
            verify(mockOrderApiNetwork).sendOrder(mockHeader, orderRequest)
            verify(mockAppProperties).getCurrencyID()
            Assert.assertNotNull(result)
            Assert.assertTrue(result is ResultState.Error)
            Assert.assertTrue((result as ResultState.Error).exception is ApiErrorException)
            Assert.assertEquals(
                (result.exception as ApiErrorException).contentMessage.value,
                "No se ha podido enviar el pedido"
            )
            Assert.assertEquals(
                (result.exception as ApiErrorException).contentMessage.info,
                "¿Quieres intentarlo nuevamente?"
            )
        }
    }


    @Test
    fun `GIVEN an order WHEN sendOrder and the api is successful but the body success is false then the message does not contain info THEN return a ResultState error OrderBusinessException with info value as empty`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = createOrder()
            val mockResponse: Response<OrderBaseResponse<OrderResponse>> =
                createApiSuccessfulBodyFalseNullInfoResponse()
            val orderRequest: OrderRequest = OrderRequest.getOrderRequest(orderMock)

            val mockHeader = mapOf(OrderNetworkService.API_HEADERS_CURRENCY_ID to "currencyId")
            whenever(mockOrderApiNetwork.sendOrder(mockHeader, orderRequest)).doReturn(mockResponse)
            whenever(mockAppProperties.getCurrencyID()).doReturn("currencyId")

            //WHEN
            val result = sutNetwork.sendOrder(orderMock)

            //THEN
            verify(mockOrderApiNetwork).sendOrder(mockHeader, orderRequest)
            verify(mockAppProperties).getCurrencyID()
            Assert.assertNotNull(result)
            Assert.assertTrue(result is ResultState.Error)
            Assert.assertTrue((result as ResultState.Error).exception is OrderBusinessException)
            Assert.assertEquals(
                (result.exception as OrderBusinessException).contentMessage.value,
                "An internal error occur"
            )
            Assert.assertEquals(
                (result.exception as OrderBusinessException).contentMessage.info,
                ""
            )
        }
    }

    @Test
    fun `GIVEN response WHEN getOrderStatus THEN is success`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: Response<BaseResponse<List<OrderStatusResponse>>> =
                createApiStateOrderSuccessResponse()
            whenever(mockOrderApiNetwork.getOrderStatus()).doReturn(mockResponse)

            //WHEN
            val result = sutNetwork.getOrderStatus()

            //THEN
            verify(mockOrderApiNetwork).getOrderStatus()
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, mockResponse.body()?.payload?.size)
        }
    }

    @Test
    fun `GIVEN response WHEN getOrderStatus respond with a null body THEN is success with an empty list`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: Response<BaseResponse<List<OrderStatusResponse>>> =
                createApiStateOrderNullBodyResponse()
            whenever(mockOrderApiNetwork.getOrderStatus()).doReturn(mockResponse)

            //WHEN
            val result = sutNetwork.getOrderStatus()

            //THEN
            verify(mockOrderApiNetwork).getOrderStatus()
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, 0)
        }
    }

    @Test(expected = Exception::class)
    fun `GIVEN exception WHEN getOrderStatus THEN throw exception`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            whenever(mockOrderApiNetwork.getOrderStatus()).doThrow(Exception("ApiException"))

            //WHEN
            sutNetwork.getOrderStatus()

            //THEN
            //Exception should be thrown
        }
    }

    private fun createOrder(): Order {
        return Order(
            code = "Chancy",
            orderType = OrderType.Delivery,
            restaurantId = "Bethanne",
            client = null,
            paymentMethod = null,
            orderDetails = arrayListOf(),
            saleOrderStrategy = null
        )
    }

    private fun createApiSuccessfulResponse(): Response<OrderBaseResponse<OrderResponse>> {
        return Response.success(
            OrderBaseResponse(
                true, message = OrderMessageResponse(
                    value = "Katelan",
                    info = null,
                    metaData = null
                ), OrderResponse(id = "Laurissa", status = "Melany")
            )
        )
    }

    private fun createApiSuccessfulBodyFalseResponse(): Response<OrderBaseResponse<OrderResponse>> {
        return Response.success(
            OrderBaseResponse(
                false, message = OrderMessageResponse(
                    value = "An internal error occur",
                    info = "be careful with this issue",
                    metaData = null
                ), OrderResponse(id = "Laurissa", status = "Melany")
            )
        )
    }

    private fun createApiSuccessfulBodyFalseNullInfoResponse(): Response<OrderBaseResponse<OrderResponse>> {
        return Response.success(
            OrderBaseResponse(
                false, message = OrderMessageResponse(
                    value = "An internal error occur",
                    info = null,
                    metaData = null
                ), OrderResponse(id = "Laurissa", status = "Melany")
            )
        )
    }

    private fun createApiUnsuccessfulResponse(): Response<OrderBaseResponse<OrderResponse>> {
        return Response.error(
            500, "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    private fun createApiUnsuccessfulBodyNullResponse(): Response<OrderBaseResponse<OrderResponse>> {
        return Response.success(null)
    }

    private fun createApiStateOrderSuccessResponse(): Response<BaseResponse<List<OrderStatusResponse>>> {
        return Response.success(
            BaseResponse(
                true, "Success", listOf(
                    OrderStatusResponse(
                        id = "Kaycee",
                        number = "Letitia",
                        date = "Candelaria",
                        state = "Viet",
                        restaurant = "Evangelos",
                        paymentMethod = null,
                        orderType = OrderTypeStatusResponse(
                            code = "Noelani",
                            title = "Leigha",
                            description = null
                        ),
                        total = "Juancarlos",
                        client = ClientStatusResponse(
                            client = "Chistopher",
                            cel = null,
                            addressReference = null
                        ),
                        detail = listOf()
                    )
                )
            )
        )
    }

    private fun createApiStateOrderNullBodyResponse(): Response<BaseResponse<List<OrderStatusResponse>>> {
        return Response.success(null)
    }
}
