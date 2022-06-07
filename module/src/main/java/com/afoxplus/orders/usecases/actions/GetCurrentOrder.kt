package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal interface GetCurrentOrder {
    suspend operator fun invoke(): SharedFlow<Order?>
}

internal class GetCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    GetCurrentOrder {
    override suspend fun invoke(): SharedFlow<Order?> {
        return orderRepository.getCurrentOrder()
    }

}