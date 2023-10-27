package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class AddOrUpdateAppetizerToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository){
     suspend operator fun invoke(quantity: Int, appetizer: Product, product: Product) {
        orderRepository.addOrUpdateAppetizerToCurrentOrder(quantity, appetizer, product)
    }
}