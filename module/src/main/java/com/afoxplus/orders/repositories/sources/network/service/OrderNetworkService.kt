package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.toEntity
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) : OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): String {
        val orderRequest = OrderRequest.getOrderRequest(order)
        val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
        orderApiNetwork.sendOrder(headerMap, orderRequest)
        return "¡Pedido enviado correctamente!"
    }

    override suspend fun getOrderStatus(): List<OrderStatus> {
        val response = orderApiNetwork.getOrderStatus()
        return response.body()?.payload?.map {
            it.toEntity()
        } ?: emptyList()
    }

    companion object {
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}