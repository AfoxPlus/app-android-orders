package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.CalculateSubTotalByProduct
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class CalculateSubTotalByProductUseCase @Inject constructor() :
    CalculateSubTotalByProduct {
    override fun invoke(quantity: Int, product: Product): Double {
        return product.getPriceForSale() * quantity
    }
}