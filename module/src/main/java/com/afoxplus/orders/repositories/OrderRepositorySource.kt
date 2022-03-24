package com.afoxplus.orders.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import javax.inject.Inject

internal class OrderRepositorySource @Inject constructor(
    private val orderLocalDataSource: OrderLocalDataSource,
    private val orderRemoteDataSource: OrderNetworkDataSource
) :
    OrderRepository {

    override fun plusItemProductToDifferentContextOrder(product: Product): Order {
        return orderLocalDataSource.plusProductDifferentContextOrder(product)
    }

    override fun lessItemProductToDifferentContextOrder(product: Product): Order {
        return orderLocalDataSource.lessProductDifferentContextOrder(product)
    }

    override fun setItemProductInDifferentContextOrder(product: Product, quantity: Int): Order {
        return orderLocalDataSource.setItemProductInDifferentContextOrder(product, quantity)
    }

    override fun updateOrderFromDifferentContext(): Order {
        return orderLocalDataSource.updateOrderFromDifferentContext()
    }

    override suspend fun sendOrder(order: Order) {
        orderRemoteDataSource.sendOrder(order)
        orderLocalDataSource.clearCurrentOrder()
    }

    override suspend fun findProductInOrder(product: Product): OrderDetail? {
        return orderLocalDataSource.findProductInOrder(product)
    }

    companion object {
        private const val ERROR_ORDER_NOT_EXIST = "The order does not exist"
    }
}