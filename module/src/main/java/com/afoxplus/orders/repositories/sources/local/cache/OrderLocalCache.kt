package com.afoxplus.orders.repositories.sources.local.cache

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.products.entities.Product
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OrderLocalCache @Inject constructor() : OrderLocalDataSource {
    private var order: Order? = null

    override fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product) {
        order?.addProductWithQuantity(product, quantity)
            ?: newOrder().addProductWithQuantity(product, quantity)
    }

    override fun clearCurrentOrder() {
        order = null
    }

    override fun findProductInOrder(product: Product): OrderDetail? {
        return order?.getOrderDetails()?.find { item -> item.product.code == product.code }
    }

    override fun getCurrentOrder(): Order {
        return order ?: throw Exception(ERROR_ORDER_IS_NULL)
    }

    private fun newOrder(): Order {
        val newOrder = Order(date = Calendar.getInstance().time)
        order = newOrder
        return newOrder
    }

    companion object {
        private const val ERROR_ORDER_IS_NULL = "Order is null"
    }
}