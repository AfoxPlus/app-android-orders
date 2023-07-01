package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.ClearCurrentOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

internal class ClearCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    ClearCurrentOrder {
    override fun invoke() = orderRepository.clearCurrentOrder()
}