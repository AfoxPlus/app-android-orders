package com.afoxplus.orders.delivery.models

import com.afoxplus.orders.domain.entities.OrderStatus

sealed class OrderStateUIModel() {
    object Loading : OrderStateUIModel()
    data class Success(val orders: List<OrderStatus>) : OrderStateUIModel()
    data class Error(val error: Exception) : OrderStateUIModel()
}