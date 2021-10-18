package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order

fun interface SendOrder {
    suspend operator fun invoke(order: Order)
}