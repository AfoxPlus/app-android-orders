package com.afoxplus.orders.delivery.views.adapters.listeners

import com.afoxplus.orders.entities.OrderDetail

interface ItemCartProductListener {
    fun deleteItem(orderDetail: OrderDetail)
    fun updateQuantity(orderDetail: OrderDetail, quantity:Int)
}