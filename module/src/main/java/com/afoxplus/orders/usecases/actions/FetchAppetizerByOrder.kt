package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.products.entities.Product

fun interface FetchAppetizerByOrder {
    suspend operator fun invoke(
        product: Product
    ): List<OrderAppetizerDetail>
}