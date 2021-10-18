package com.afoxplus.orders.repositories.sources.local

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

internal interface OrderLocalDataSource {
    suspend fun clearCurrentOrder()
    suspend fun updateOrder(order: Order)
    suspend fun findOrder(): Order?
    suspend fun newOrder(): Order
    suspend fun addProduct(product: Product, quantity: Int)
    suspend fun findProductInOrder(product: Product): OrderDetail?
}