package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal interface GetCurrentOrderFlow {
    suspend operator fun invoke(): SharedFlow<Order?>
}

internal class GetCurrentOrderFlowUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    GetCurrentOrderFlow {
    override suspend fun invoke(): SharedFlow<Order?> {
        return orderRepository.getCurrentOrderFlow()
    }

}