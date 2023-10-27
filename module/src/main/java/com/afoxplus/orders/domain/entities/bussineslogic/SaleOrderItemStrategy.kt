package com.afoxplus.orders.domain.entities.bussineslogic

import android.os.Parcelable
import com.afoxplus.orders.domain.entities.OrderDetail

interface SaleOrderItemStrategy: Parcelable {
    fun calculateSubTotal(orderDetail: OrderDetail): Double
}