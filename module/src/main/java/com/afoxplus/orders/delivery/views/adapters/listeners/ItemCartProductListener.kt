package com.afoxplus.orders.delivery.views.adapters.listeners

import com.afoxplus.orders.entities.OrderDetail

internal interface ItemCartProductListener {
    fun deleteItem(orderDetail: OrderDetail)
    fun updateQuantity(orderDetail: OrderDetail, quantity: Int)

    fun editProduct(orderDetail: OrderDetail)
}