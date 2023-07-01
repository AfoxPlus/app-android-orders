package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.DeleteProductToCurrentOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class DeleteProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    DeleteProductToCurrentOrder {
    override suspend fun invoke(product: Product) =
        orderRepository.deleteProductToCurrentOrder(product)
}