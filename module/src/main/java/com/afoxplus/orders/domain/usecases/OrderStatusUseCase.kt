package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.orders.domain.repositories.OrderStatusRepository
import javax.inject.Inject

internal class OrderStatusUseCase @Inject constructor(
    private val repository: OrderStatusRepository
) {

    suspend operator fun invoke(): List<OrderStatus> {
        return repository.getOrderStatus()
    }

}