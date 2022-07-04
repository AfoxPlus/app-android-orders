package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import javax.inject.Inject

fun interface SendOrder {
    suspend operator fun invoke(order: Order): String
}

internal class SendOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SendOrder {
    override suspend fun invoke(order: Order): String {
        return orderRepository.sendOrder(order)
    }
}