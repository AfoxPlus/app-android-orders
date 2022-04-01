package com.afoxplus.orders.repositories.sources.network


import com.afoxplus.orders.entities.Order
import javax.inject.Inject

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order)
}

internal class OrderNetworkDataSourceImpl @Inject constructor() : OrderNetworkDataSource {
    override suspend fun sendOrder(order: Order) {
        //Do nothing
    }
}