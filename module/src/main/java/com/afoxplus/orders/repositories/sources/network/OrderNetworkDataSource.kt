package com.afoxplus.orders.repositories.sources.network

import android.util.Log
import com.afoxplus.orders.entities.Order
import javax.inject.Inject

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order)
}

internal class OrderNetworkDataSourceImpl @Inject constructor() : OrderNetworkDataSource {
    override suspend fun sendOrder(order: Order) {
        Log.d("LOG_ORDER", "Send remote order success $order")
    }
}