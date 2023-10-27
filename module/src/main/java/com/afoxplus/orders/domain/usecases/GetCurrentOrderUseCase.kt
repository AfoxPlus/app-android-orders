package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.repositories.OrderRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class GetCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(): SharedFlow<Order?> {
        return orderRepository.getCurrentOrder()
    }
}