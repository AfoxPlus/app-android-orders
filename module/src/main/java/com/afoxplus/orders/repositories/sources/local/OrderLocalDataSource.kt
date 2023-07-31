package com.afoxplus.orders.repositories.sources.local

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.SharedFlow

internal interface OrderLocalDataSource {
    suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product)
    fun clearCurrentOrder()
    fun findProductInOrder(product: Product): OrderDetail?
    suspend fun getCurrentOrder(): SharedFlow<Order?>
    suspend fun deleteProductToCurrentOrder(product: Product)
    suspend fun addOrUpdateAppetizerToCurrentOrder(quantity: Int, appetizer: Product, product: Product)
    suspend fun fetchAppetizersByProduct(product: Product): List<OrderAppetizerDetail>
    suspend fun clearAppetizersByProduct(product: Product)
}