package com.afoxplus.orders.usecases.actions

import com.afoxplus.products.entities.Product

internal fun interface CalculateSubTotalByProduct {
    operator fun invoke(quantity: Int, product: Product): Double
}