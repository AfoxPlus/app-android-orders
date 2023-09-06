package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.usecases.repositories.OrderStatusRepository
import com.afoxplus.uikit.result.ResultState
import javax.inject.Inject

internal class OrderStatusUseCase @Inject constructor(
    private val repository: OrderStatusRepository
) {

    suspend fun getStatusOrders(): ResultState<List<OrderStatus>> {
        return repository.getOrderStatus()
    }

}