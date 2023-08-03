package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.orders.usecases.actions.MatchAppetizersByOrder
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class MatchAppetizersByOrderUseCase @Inject constructor(
    val fetchAppetizerByOrderUseCase: FetchAppetizerByOrderUseCase
) : MatchAppetizersByOrder {
    override suspend fun invoke(
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