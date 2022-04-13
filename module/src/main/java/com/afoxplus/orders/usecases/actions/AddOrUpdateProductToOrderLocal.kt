package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal interface AddOrUpdateProductToOrderLocal {
    operator fun invoke(quantity: Int, product: Product)
}

internal class AddOrUpdateProductToOrderLocalUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    AddOrUpdateProductToOrderLocal {
    override fun invoke(quantity: Int, product: Product) {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product)
    }
}