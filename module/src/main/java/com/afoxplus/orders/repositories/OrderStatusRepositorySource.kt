package com.afoxplus.orders.repositories

import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.usecases.repositories.OrderStatusRepository
import javax.inject.Inject

internal class OrderStatusRepositorySource @Inject constructor(
    private val dataSourceRemote: OrderNetworkDataSource
) : OrderStatusRepository {

    override suspend fun getOrderStatus(): List<OrderStatus> {
        return dataSourceRemote.getOrderStatus()
    }

}