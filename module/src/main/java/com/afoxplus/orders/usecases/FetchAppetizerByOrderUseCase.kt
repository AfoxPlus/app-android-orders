package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.orders.usecases.actions.FetchAppetizerByOrder
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class FetchAppetizerByOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    FetchAppetizerByOrder {
    override suspend fun invoke(product: Product): List<OrderAppetizerDetail> {
        return orderRepository.fetchAppetizersByProduct(product)
    }
}