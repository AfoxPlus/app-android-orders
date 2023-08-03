package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import kotlinx.coroutines.flow.SharedFlow

fun interface GetCurrentOrder {
    suspend operator fun invoke(): SharedFlow<Order?>
}