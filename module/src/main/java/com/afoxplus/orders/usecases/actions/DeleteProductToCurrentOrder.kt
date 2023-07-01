package com.afoxplus.orders.usecases.actions

import com.afoxplus.products.entities.Product

internal fun interface DeleteProductToCurrentOrder {
    suspend operator fun invoke(product: Product)
}

