package com.afoxplus.orders.entities.bussineslogic

import android.os.Parcelable
import com.afoxplus.orders.entities.Order

interface SaleOrderStrategy : Parcelable {
    fun calculateTotal(order: Order): Double
}