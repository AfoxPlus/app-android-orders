package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.products.entities.Product

fun interface MatchAppetizersByOrder {
    suspend operator fun invoke(
        appetizers: List<Product>,
        product: Product
    ): List<OrderAppetizerDetail>
}
