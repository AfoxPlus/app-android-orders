package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject


internal class AddOrUpdateProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(quantity: Int, product: Product) {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product)
    }
}