package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

fun interface FindProductInOrder {
    operator fun invoke(product: Product): OrderDetail?
}