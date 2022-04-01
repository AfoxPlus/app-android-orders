package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

internal interface LessItemProductToDifferentContextOrder {
    suspend operator fun invoke(product: Product): Flow<Result<Order>>
}

@Singleton
internal class LessItemProductToDifferentContextOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    LessItemProductToDifferentContextOrder {
    override suspend fun invoke(product: Product): Flow<Result<Order>> = flow {
        try {
            val order = orderRepository.lessItemProductToDifferentContextOrder(product)
            emit(Result.success(order))
        } catch (ex: Throwable) {
            emit(Result.failure(ex))
        }
    }
}