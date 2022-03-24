package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal interface SetItemProductInDifferentContextOrder {
    operator fun invoke(product: Product, quantity: Int): Flow<Result<Order>>
}

internal class SetItemProductInDifferentContextOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SetItemProductInDifferentContextOrder {
    override fun invoke(product: Product, quantity: Int): Flow<Result<Order>> = flow {
        try {
            val order = orderRepository.setItemProductInDifferentContextOrder(product, quantity)
            emit(Result.success(order))
        } catch (ex: Throwable) {
            emit(Result.failure(ex))
        }

    }
}