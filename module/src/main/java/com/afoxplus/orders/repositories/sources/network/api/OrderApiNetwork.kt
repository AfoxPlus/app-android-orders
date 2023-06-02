package com.afoxplus.orders.repositories.sources.network.api

import com.afoxplus.network.response.BaseResponse
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface OrderApiNetwork {

    companion object {
        const val PATH_ORDERS = "orders"
        const val PATH_VERSION_01 = "v1"
        const val PATH_SEND = "send"
    }

    @POST("$PATH_ORDERS/$PATH_VERSION_01/$PATH_SEND")
    suspend fun sendOrder(@Body orderRequest: OrderRequest): Response<BaseResponse<OrderResponse>>
}