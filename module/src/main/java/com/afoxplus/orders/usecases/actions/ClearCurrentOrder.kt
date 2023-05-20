package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

internal interface ClearCurrentOrder {
    operator fun invoke()
}

internal class ClearCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    ClearCurrentOrder {
    override fun invoke() = orderRepository.clearCurrentOrder()

}