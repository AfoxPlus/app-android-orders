package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

interface FindProductInOrder {
    operator fun invoke(product: Product): OrderDetail?
}

internal class FindProductInOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    FindProductInOrder {
    override fun invoke(product: Product): OrderDetail? {
        return orderRepository.findProductInCurrentOrder(product)
    }
}