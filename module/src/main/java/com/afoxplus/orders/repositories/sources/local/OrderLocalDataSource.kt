package com.afoxplus.orders.repositories.sources.local

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

internal interface OrderLocalDataSource {
    fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product)
    fun clearCurrentOrder()
    fun findProductInOrder(product: Product): OrderDetail?
    fun getCurrentOrder(): Order
}