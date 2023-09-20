package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.repositories.OrderRepository
import javax.inject.Inject

internal class ClearCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
   operator fun invoke() = orderRepository.clearCurrentOrder()
}