package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order

fun interface FindOrder {
    suspend operator fun invoke(): Order
}