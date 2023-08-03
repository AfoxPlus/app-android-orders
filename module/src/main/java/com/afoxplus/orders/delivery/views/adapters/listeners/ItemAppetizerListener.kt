package com.afoxplus.orders.delivery.views.adapters.listeners

import com.afoxplus.products.entities.Product

internal interface ItemAppetizerListener {
    fun updateQuantity(product: Product, quantity:Int)
}