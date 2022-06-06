package com.afoxplus.orders.repositories.sources.local

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.SharedFlow

internal interface OrderLocalDataSource {
    suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product)
    fun clearCurrentOrder()
    fun findProductInOrder(product: Product): OrderDetail?

    @Deprecated(
        "This method is no longer valid, to get the current order you must get it from getCurrentOrderFlow",
        ReplaceWith("fun getCurrentOrderFlow(): StateFlow<Order>")
    )
    fun getCurrentOrder(): Order
    suspend fun getCurrentOrderFlow(): SharedFlow<Order?>
}