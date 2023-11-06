package com.afoxplus.orders.domain.usecases

import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class MatchAppetizersByOrderUseCase @Inject constructor(
    private val fetchAppetizerByOrderUseCase: FetchAppetizerByOrderUseCase
) {
    suspend operator fun invoke(
        appetizers: List<Product>,
        product: Product
    ): List<OrderAppetizerDetail> {
        val orderAppetizers = fetchAppetizerByOrderUseCase(product)
        return appetizers.map { appetizer ->
            orderAppetizers.find { appetizer.code == it.product.code }
                ?.apply { OrderAppetizerDetail(appetizer, quantity) }
                ?: OrderAppetizerDetail(appetizer, 0)
        }
    }
}