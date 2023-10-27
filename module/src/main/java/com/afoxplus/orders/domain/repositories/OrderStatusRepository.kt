package com.afoxplus.orders.domain.repositories

import com.afoxplus.orders.domain.entities.OrderStatus

interface OrderStatusRepository {
    suspend fun getOrderStatus(): List<OrderStatus>
}