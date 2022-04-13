package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

internal interface GetCurrentOrder {
    operator fun invoke(): Order
}

internal class GetCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    GetCurrentOrder {
    override fun invoke(): Order = orderRepository.getCurrentOrder()

}