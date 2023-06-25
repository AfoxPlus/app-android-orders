package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) :
    OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): String {
        val orderRequest = OrderRequest.getOrderRequest(order)
        val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
        orderApiNetwork.sendOrder(headerMap, orderRequest)
        return "¡Pedido enviado correctamente!"
    }

    companion object {
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}