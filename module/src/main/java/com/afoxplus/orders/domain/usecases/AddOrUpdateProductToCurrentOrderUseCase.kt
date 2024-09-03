package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject


internal class AddOrUpdateProductToCurrentOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(quantity: Int, product: Product, notes: String? = "") {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product, notes.toString())
    }
    suspend operator fun invoke(quantity: Int, orderDetail: OrderDetail) {
        orderRepository.addOrUpdateProductToCurrentOrder(quantity, product = orderDetail.product, notes = orderDetail.notes)
    }
}