package com.afoxplus.orders.domain.repositories

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.common.ResultState
import kotlinx.coroutines.flow.SharedFlow

interface OrderRepository {
    suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product, notes: String)
    fun findProductInCurrentOrder(product: Product): OrderDetail?
    fun clearCurrentOrder()
    suspend fun getCurrentOrder(): SharedFlow<Order?>
    suspend fun deleteProductToCurrentOrder(product: Product)
    suspend fun sendOrder(order: Order): ResultState<String>
    suspend fun addOrUpdateAppetizerToCurrentOrder(
        quantity: Int,
        appetizer: Product,
        product: Product
    )

    suspend fun fetchAppetizersByProduct(product: Product): List<OrderAppetizerDetail>

    suspend fun clearAppetizersByProduct(product: Product)
}