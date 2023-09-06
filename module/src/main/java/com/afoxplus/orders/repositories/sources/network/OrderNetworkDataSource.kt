package com.afoxplus.orders.repositories.sources.network

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.uikit.result.ResultState

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order): ResultState<String>
    suspend fun getOrderStatus(): ResultState<List<OrderStatus>>
}
