package com.afoxplus.orders.data.repositories

import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.orders.data.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.domain.repositories.OrderStatusRepository
import javax.inject.Inject

internal class OrderStatusRepositorySource @Inject constructor(
    private val dataSourceRemote: OrderNetworkDataSource
) : OrderStatusRepository {

    override suspend fun getOrderStatus(): List<OrderStatus> {
        return dataSourceRemote.getOrderStatus()
    }

}