package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.SharedFlow

interface OrderRepository {
    suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product)
    fun findProductInCurrentOrder(product: Product): OrderDetail?
    fun clearCurrentOrder()
    fun getCurrentOrder(): Order
    suspend fun getCurrentOrderFlow(): SharedFlow<Order?>

    suspend fun sendOrder(order: Order)

}