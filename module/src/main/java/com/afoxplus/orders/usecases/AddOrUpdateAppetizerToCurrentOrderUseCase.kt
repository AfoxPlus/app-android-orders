package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.AddOrUpdateAppetizerToCurrentOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class AddOrUpdateAppetizerToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    AddOrUpdateAppetizerToCurrentOrder {
    override suspend fun invoke(quantity: Int, appetizer: Product, product: Product) {
        orderRepository.addOrUpdateAppetizerToCurrentOrder(quantity, appetizer, product)
    }
}