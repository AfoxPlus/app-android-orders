package com.afoxplus.orders.repositories.sources.local.cache

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.products.entities.Product
import java.util.*
import javax.inject.Inject

internal class OrderLocalCache @Inject constructor() : OrderLocalDataSource {
    private var order: Order? = null

    override suspend fun clearCurrentOrder() {
        order = null
    }

    override suspend fun updateOrder(order: Order) {
        this.order = order
    }

    override suspend fun findOrder(): Order? {
        return this.order
    }

    override suspend fun newOrder(): Order {
        val newOrder = Order(date = Calendar.getInstance().time)
        order = newOrder
        return newOrder
    }

    override suspend fun addProduct(product: Product, quantity: Int) {
        order?.addProduct(product, quantity)
    }

    override suspend fun findProductInOrder(product: Product): OrderDetail? {
        return order?.getOrderDetails()?.find { item -> item.product.code == product.code }
    }
}