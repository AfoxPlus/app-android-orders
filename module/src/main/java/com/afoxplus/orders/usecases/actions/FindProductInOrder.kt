package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

interface FindProductInOrder {
    suspend operator fun invoke(product: Product): OrderDetail?
}