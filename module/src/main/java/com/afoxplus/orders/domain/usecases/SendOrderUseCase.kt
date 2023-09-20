package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.uikit.common.ResultState
import javax.inject.Inject

internal class SendOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: Order): ResultState<String> {
        return orderRepository.sendOrder(order)
    }
}