package com.afoxplus.orders.repositories

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class OrderRepositorySource @Inject constructor(
    private val orderLocalDataSource: OrderLocalDataSource,
    private val orderRemoteDataSource: OrderNetworkDataSource
) :
    OrderRepository {

    override fun clearCurrentOrder() {
        orderLocalDataSource.clearCurrentOrder()
    }

    override fun getCurrentOrder(): Order {
        return orderLocalDataSource.getCurrentOrder()
    }

    override suspend fun getCurrentOrderFlow(): SharedFlow<Order?> {
        return orderLocalDataSource.getCurrentOrderFlow()
    }

    override suspend fun sendOrder(order: Order) {
        orderRemoteDataSource.sendOrder(order)
        orderLocalDataSource.clearCurrentOrder()
    }

    override suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product) {
        orderLocalDataSource.addOrUpdateProductToCurrentOrder(quantity, product)
    }

    override fun findProductInCurrentOrder(product: Product): OrderDetail? {
        return orderLocalDataSource.findProductInOrder(product)
    }
}