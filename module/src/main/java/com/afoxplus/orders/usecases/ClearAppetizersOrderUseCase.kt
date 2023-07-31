package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.ClearAppetizersOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

class ClearAppetizersOrderUseCase @Inject constructor(val orderRepository: OrderRepository) :
    ClearAppetizersOrder {
    override suspend fun invoke(product: Product) =
        orderRepository.clearAppetizersByProduct(product)
}