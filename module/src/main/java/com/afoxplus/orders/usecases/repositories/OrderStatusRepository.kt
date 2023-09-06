package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.uikit.result.ResultState

interface OrderStatusRepository {
    suspend fun getOrderStatus(): ResultState<List<OrderStatus>>
}