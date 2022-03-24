package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal interface UpdateOrderFromDifferentContext {
    operator fun invoke(): Flow<Result<Order>>
}

internal class UpdateOrderFromDifferentContextUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    UpdateOrderFromDifferentContext {
    override fun invoke(): Flow<Result<Order>> = flow {
        try {
            val order = orderRepository.updateOrderFromDifferentContext()
            emit(Result.success(order))
        } catch (ex: Throwable) {
            emit(Result.failure(ex))
        }
    }
}