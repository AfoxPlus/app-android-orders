package com.afoxplus.orders.usecases.actions

import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal interface CalculateSubTotalByProduct {
    operator fun invoke(quantity: Int, product: Product): Double
}

internal class CalculateSubTotalByProductUseCase @Inject constructor() : CalculateSubTotalByProduct {
    override fun invoke(quantity: Int, product: Product): Double {
        return product.getPriceForSale() * quantity
    }
}