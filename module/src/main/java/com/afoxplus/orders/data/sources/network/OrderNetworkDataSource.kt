package com.afoxplus.orders.data.sources.network

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.uikit.common.ResultState

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order): ResultState<String>
    suspend fun getOrderStatus(): List<OrderStatus>
}
