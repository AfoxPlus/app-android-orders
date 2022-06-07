package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal interface DeleteProductToCurrentOrder {
    suspend operator fun invoke(product: Product)
}

internal class DeleteProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    DeleteProductToCurrentOrder {
    override suspend fun invoke(product: Product) =
        orderRepository.deleteProductToCurrentOrder(product)
}
