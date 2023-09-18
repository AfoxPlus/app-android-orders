package com.afoxplus.orders.data.repositories

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.data.sources.local.OrderLocalDataSource
import com.afoxplus.orders.data.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.common.ResultState
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

    override suspend fun getCurrentOrder(): SharedFlow<Order?> {
        return orderLocalDataSource.getCurrentOrder()
    }

    override suspend fun deleteProductToCurrentOrder(product: Product) =
        orderLocalDataSource.deleteProductToCurrentOrder(product)

    override suspend fun sendOrder(order: Order): ResultState<String> {
        val message = orderRemoteDataSource.sendOrder(order)
        orderLocalDataSource.clearCurrentOrder()
        return message
    }

    override suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product) {
        orderLocalDataSource.addOrUpdateProductToCurrentOrder(quantity, product)
    }

    override fun findProductInCurrentOrder(product: Product): OrderDetail? {
        return orderLocalDataSource.findProductInOrder(product)
    }

    override suspend fun addOrUpdateAppetizerToCurrentOrder(
        quantity: Int,
        appetizer: Product,
        product: Product
    ) {
        orderLocalDataSource.addOrUpdateAppetizerToCurrentOrder(quantity, appetizer, product)
    }

    override suspend fun fetchAppetizersByProduct(product: Product): List<OrderAppetizerDetail> {
        return orderLocalDataSource.fetchAppetizersByProduct(product)
    }

    override suspend fun clearAppetizersByProduct(product: Product) =
        orderLocalDataSource.clearAppetizersByProduct(product)
}