package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(private val orderApiNetwork: OrderApiNetwork) :
    OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): String {
        val orderRequest = OrderRequest.getOrderRequest(order)
        orderApiNetwork.sendOrder(orderRequest)
        return "¡Pedido enviado correctamente!"
    }
}