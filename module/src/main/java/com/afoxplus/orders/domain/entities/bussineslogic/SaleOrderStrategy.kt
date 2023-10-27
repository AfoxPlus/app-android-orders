package com.afoxplus.orders.domain.entities.bussineslogic

import android.os.Parcelable
import com.afoxplus.orders.domain.entities.Order

interface SaleOrderStrategy : Parcelable {
    fun calculateTotal(order: Order): Double
}