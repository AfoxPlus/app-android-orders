package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal fun interface AddOrUpdateProductToCurrentOrder {
    suspend operator fun invoke(quantity: Int, product: Product)
}

internal class AddOrUpdateProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    AddOrUpdateProductToCurrentOrder {
    override suspend fun invoke(quantity: Int, product: Product) {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product)
    }
}