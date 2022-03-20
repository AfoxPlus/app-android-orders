package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product

interface OrderRepository {
    suspend fun addProduct(product: Product, quantity: Int): Order
    suspend fun findProductInCart(product: Product): OrderDetail?
}