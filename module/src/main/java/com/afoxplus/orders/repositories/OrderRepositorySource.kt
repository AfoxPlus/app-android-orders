package com.afoxplus.orders.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class OrderRepositorySource @Inject constructor(
    private val orderLocalDataSource: OrderLocalDataSource
) :
    OrderRepository {

    override suspend fun addProduct(product: Product, quantity: Int): Order {
        var order = orderLocalDataSource.findOrder()
        if (order == null) {
            order = orderLocalDataSource.newOrder()
        }
        orderLocalDataSource.addProduct(product, quantity)
        return order
    }

    override suspend fun findProductInCart(product: Product): OrderDetail? =
        orderLocalDataSource.findProductInOrder(product)
}