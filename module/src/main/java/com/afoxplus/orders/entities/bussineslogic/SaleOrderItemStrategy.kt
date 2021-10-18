package com.afoxplus.orders.entities.bussineslogic

import android.os.Parcelable
import com.afoxplus.orders.entities.OrderDetail

interface SaleOrderItemStrategy: Parcelable {
    fun calculateSubTotal(orderDetail: OrderDetail): Double
}