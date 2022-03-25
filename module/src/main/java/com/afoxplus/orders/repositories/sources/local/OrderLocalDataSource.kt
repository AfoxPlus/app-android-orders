package com.afoxplus.orders.repositories.sources.local

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

internal interface OrderLocalDataSource {
    fun plusProductDifferentContextOrder(product: Product): Order
    fun lessProductDifferentContextOrder(product: Product): Order
    fun setItemProductInDifferentContextOrder(product: Product, quantity: Int): Order
    fun updateProductInDifferentContextOrder(product: Product): Order

    fun clearCurrentOrder()
    fun findOrder(): Order?
    fun newOrder(): Order
    fun findProductInOrder(product: Product): OrderDetail?
}