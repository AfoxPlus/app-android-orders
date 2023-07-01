package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject


internal class AddOrUpdateProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    AddOrUpdateProductToCurrentOrder {
    override suspend fun invoke(quantity: Int, product: Product) {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product)
    }
}