package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class DeleteProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(product: Product) =
        orderRepository.deleteProductToCurrentOrder(product)
}