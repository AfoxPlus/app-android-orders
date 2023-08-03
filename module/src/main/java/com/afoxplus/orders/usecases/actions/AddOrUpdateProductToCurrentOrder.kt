package com.afoxplus.orders.usecases.actions

import com.afoxplus.products.entities.Product

fun interface AddOrUpdateProductToCurrentOrder  {
    suspend operator fun invoke(quantity: Int, product: Product)
}