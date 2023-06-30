package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.OrderStatus

interface OrderStatusRepository {
    suspend fun getOrderStatus(): List<OrderStatus>
}