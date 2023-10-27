package com.afoxplus.orders.domain.usecases

import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class CalculateSubTotalByProductUseCase @Inject constructor() {
    operator fun invoke(quantity: Int, product: Product): Double {
        return product.getPriceForSale() * quantity
    }
}