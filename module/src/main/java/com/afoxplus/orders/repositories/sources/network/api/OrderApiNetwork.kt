package com.afoxplus.orders.repositories.sources.network.api

import com.afoxplus.network.annotations.EndpointInfo
import com.afoxplus.network.annotations.ServiceClient
import com.afoxplus.network.api.UrlProvider
import com.afoxplus.network.response.BaseResponse
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.OrderBaseResponse
import com.afoxplus.orders.repositories.sources.network.api.response.OrderResponse
import com.afoxplus.orders.repositories.sources.network.api.response.OrderStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.GET
import retrofit2.http.POST

@ServiceClient(type = UrlProvider.Type.API_ORDERS)
internal interface OrderApiNetwork {

    companion object {
        const val PATH_ORDERS = "orders"
        const val PATH_SEND = "send"
        const val PATH_STATUS = "status"
    }

    @POST("$PATH_ORDERS/$PATH_SEND")
    @EndpointInfo(type = UrlProvider.Type.API_ORDERS_V1)
    suspend fun sendOrder(
        @HeaderMap headers: Map<String, String>,
        @Body orderRequest: OrderRequest
    ): Response<OrderBaseResponse<OrderResponse>>

    @GET("$PATH_ORDERS/$PATH_STATUS")
    @EndpointInfo(type = UrlProvider.Type.API_ORDERS)
    suspend fun getOrderStatus(): Response<BaseResponse<List<OrderStatusResponse>>>

}