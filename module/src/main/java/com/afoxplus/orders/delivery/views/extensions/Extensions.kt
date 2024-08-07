package com.afoxplus.orders.delivery.views.extensions

import com.google.gson.Gson

internal fun Double.getAmountFormat(): String {
    return "S/ ${String.format("%.2f", this)}"
}

internal inline fun <reified O> stringToObject(data: String): O {
    return Gson().fromJson(data, O::class.java)
}