package com.afoxplus.orders.repositories.sources.network.api

import com.afoxplus.network.annotations.EndpointInfo
import com.afoxplus.network.annotations.ServiceClient
import com.afoxplus.network.api.UrlProvider
import com.afoxplus.network.response.BaseResponse
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

@ServiceClient(type = UrlProvider.Type.API_ORDERS)
internal interface OrderApiNetwork {

    companion object {
        const val PATH_ORDERS = "orders"
        const val PATH_SEND = "send"
    }

    @POST("$PATH_ORDERS/$PATH_SEND")
    @EndpointInfo(type = UrlProvider.Type.API_ORDERS_V1)
    suspend fun sendOrder(@HeaderMap headers: Map<String, String>, @Body orderRequest: OrderRequest): Response<BaseResponse<OrderResponse>>
}