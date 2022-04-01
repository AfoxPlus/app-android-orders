package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

internal interface ClearLocalOrder {
    operator fun invoke()
}

internal class ClearLocalOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    ClearLocalOrder {
    override fun invoke() = orderRepository.clearLocalOrder()

}