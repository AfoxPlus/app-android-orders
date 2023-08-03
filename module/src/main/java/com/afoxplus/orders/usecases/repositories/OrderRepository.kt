package com.afoxplus.orders.usecases.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.SharedFlow

interface OrderRepository {
    suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product)
    fun findProductInCurrentOrder(product: Product): OrderDetail?
    fun clearCurrentOrder()
    suspend fun getCurrentOrder(): SharedFlow<Order?>
    suspend fun deleteProductToCurrentOrder(product: Product)
    suspend fun sendOrder(order: Order): String
    suspend fun addOrUpdateAppetizerToCurrentOrder(
        quantity: Int,
        appetizer: Product,
        product: Product
    )

    suspend fun fetchAppetizersByProduct(product: Product): List<OrderAppetizerDetail>

    suspend fun clearAppetizersByProduct(product: Product)
}