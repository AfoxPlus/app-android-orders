package com.afoxplus.orders.repositories.sources.network


import com.afoxplus.orders.entities.Order
import javax.inject.Inject

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order): String
}

internal class OrderNetworkDataSourceImpl @Inject constructor() : OrderNetworkDataSource {
    override suspend fun sendOrder(order: Order): String {
        //Do nothing
        return "Pedido enviado exitosamente!"
    }
}