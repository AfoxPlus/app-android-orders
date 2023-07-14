package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.usecases.repositories.OrderStatusRepository
import javax.inject.Inject

internal class OrderStatusUseCase @Inject constructor(
    private val repository: OrderStatusRepository
) {

    suspend fun getStatusOrders(): List<OrderStatus> {
        return repository.getOrderStatus()
    }

}