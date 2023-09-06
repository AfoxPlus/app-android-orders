package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.uikit.result.ResultState

fun interface SendOrder {
    suspend operator fun invoke(order: Order): ResultState<String>
}