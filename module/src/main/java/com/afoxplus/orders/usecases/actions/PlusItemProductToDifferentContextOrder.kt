package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import com.afoxplus.products.usecases.actions.HasProductStock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

internal interface PlusItemProductToDifferentContextOrder {
    suspend operator fun invoke(product: Product): Flow<Result<Order>>
}

internal class PlusItemProductToDifferentContextOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val hasProductStock: HasProductStock
) : PlusItemProductToDifferentContextOrder {
    override suspend fun invoke(product: Product): Flow<Result<Order>> = flow {
        try {
            if (hasProductStock(product, 1)) {
                val order = orderRepository.plusItemProductToDifferentContextOrder(product)
                emit(Result.success(order))
            } else emit(Result.failure(IOException("Has not stock product")))
        } catch (ex: Throwable) {
            emit(Result.failure(ex))
        }
    }
}