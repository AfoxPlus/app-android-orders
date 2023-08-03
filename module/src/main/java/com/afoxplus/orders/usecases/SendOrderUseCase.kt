package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.actions.SendOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

internal class SendOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SendOrder {
    override suspend fun invoke(order: Order): String {
        return orderRepository.sendOrder(order)
    }
}