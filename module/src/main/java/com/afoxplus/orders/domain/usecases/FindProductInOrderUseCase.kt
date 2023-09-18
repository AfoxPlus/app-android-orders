package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class FindProductInOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    operator fun invoke(product: Product): OrderDetail? {
        return orderRepository.findProductInCurrentOrder(product)
    }
}