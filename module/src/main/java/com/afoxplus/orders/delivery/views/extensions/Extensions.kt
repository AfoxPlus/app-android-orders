package com.afoxplus.orders.delivery.views.extensions

internal fun Double.getAmountFormat(): String {
    return "S/ ${String.format("%.2f", this)}"
}