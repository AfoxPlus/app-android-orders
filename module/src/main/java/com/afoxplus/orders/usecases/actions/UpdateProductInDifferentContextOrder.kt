package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal interface UpdateProductInDifferentContextOrder {
    operator fun invoke(product: Product): Flow<Result<Order>>
}

internal class UpdateProductInDifferentContextOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    UpdateProductInDifferentContextOrder {
    override fun invoke(product: Product): Flow<Result<Order>> = flow {
        try {
            val order = orderRepository.updateProductInDifferentContextOrder(product)
            emit(Result.success(order))
        } catch (ex: Throwable) {
            emit(Result.failure(ex))
        }
    }
}