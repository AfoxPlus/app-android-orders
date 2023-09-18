package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

class ClearAppetizersOrderUseCase @Inject constructor(val orderRepository: OrderRepository) {
    suspend operator fun invoke(product: Product) =
        orderRepository.clearAppetizersByProduct(product)
}