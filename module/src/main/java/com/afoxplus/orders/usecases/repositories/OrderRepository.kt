package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

interface OrderRepository {
    fun plusItemProductToDifferentContextOrder(product: Product): Order
    fun lessItemProductToDifferentContextOrder(product: Product): Order
    fun setItemProductInDifferentContextOrder(product: Product, quantity: Int): Order
    fun updateProductInDifferentContextOrder(product: Product): Order
    fun clearLocalOrder()

    suspend fun sendOrder(order: Order)
    suspend fun findProductInOrder(product: Product): OrderDetail?
}