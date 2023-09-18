package com.afoxplus.orders.domain.entities

enum class OrderType(var code: String, var title: String, var description: String? = null) {
    Local(code = "SALON", title = "Mesa"),
    Delivery(code = "DELI", title = "Delivery");

    override fun toString(): String {
        val newDescription = if (description.isNullOrEmpty()) "" else ": $description"
        return "$title$newDescription"
    }
}