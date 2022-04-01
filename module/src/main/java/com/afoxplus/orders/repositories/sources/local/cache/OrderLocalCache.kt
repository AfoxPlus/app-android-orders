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
    private var orderDifferentContext: Order? = null

    override fun plusProductDifferentContextOrder(product: Product): Order {
        orderDifferentContext = findOrderDifferentContext()
        if (orderDifferentContext == null) {
            newOrderDifferentContext()
            orderDifferentContext?.plusItemProduct(product)
        } else {
            orderDifferentContext?.plusItemProduct(product)
        }
        return orderDifferentContext ?: throw Exception(ERROR_ORDER_IS_NULL)
    }

    override fun lessProductDifferentContextOrder(product: Product): Order {
        orderDifferentContext = findOrderDifferentContext()
        orderDifferentContext?.lessItemProduct(product)
        return orderDifferentContext ?: throw Exception(ERROR_ORDER_IS_NULL)
    }

    override fun setItemProductInDifferentContextOrder(product: Product, quantity: Int): Order {
        clearOrderDifferentContext()
        newOrderDifferentContext()
        orderDifferentContext?.addProduct(product, quantity)
        return orderDifferentContext ?: throw Exception(ERROR_ORDER_IS_NULL)
    }

    override fun clearCurrentOrder() {
        this.orderDifferentContext = null
        order = null
    }

    override fun updateProductInDifferentContextOrder(product: Product): Order {
        findOrder() ?: newOrder()
        orderDifferentContext?.getOrderDetailByProduct(product)?.let { orderDetail ->
            order?.removeItemOrderDetailByProduct(product)
            order?.addProduct(product, orderDetail.quantity)
        } ?: order?.removeItemOrderDetailByProduct(product)
        orderDifferentContext = null
        return this.order ?: throw Exception(ERROR_ORDER_IS_NULL)
    }

    override fun findOrder(): Order? = this.order

    private fun findOrderDifferentContext(): Order? {
        return this.orderDifferentContext
    }

    private fun newOrderDifferentContext() {
        orderDifferentContext = Order(date = Calendar.getInstance().time)
    }

    override fun newOrder(): Order {
        val newOrder = Order(date = Calendar.getInstance().time)
        order = newOrder
        return newOrder
    }

    override fun findProductInOrder(product: Product): OrderDetail? {
        return order?.getOrderDetails()?.find { item -> item.product.code == product.code }
    }

    private fun clearOrderDifferentContext() {
        this.orderDifferentContext = null
    }

    companion object {
        private const val ERROR_ORDER_IS_NULL = "Order is null"
    }
}