package com.afoxplus.orders.repositories.sources.network


import com.afoxplus.orders.entities.Order

internal interface OrderNetworkDataSource {
    suspend fun sendOrder(order: Order): String
}