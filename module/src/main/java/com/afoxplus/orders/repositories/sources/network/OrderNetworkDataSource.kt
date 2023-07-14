package com.afoxplus.orders.repositories.sources.network

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderStatus

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order): String
    suspend fun getOrderStatus(): List<OrderStatus>
}