package com.afoxplus.orders.usecases.actions

import com.afoxplus.products.entities.Product

fun interface AddOrUpdateAppetizerToCurrentOrder {
    suspend operator fun invoke(quantity: Int, appetizer: Product, product: Product)
}