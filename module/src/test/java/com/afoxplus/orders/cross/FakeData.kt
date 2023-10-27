package com.afoxplus.orders.cross

import com.afoxplus.orders.data.sources.network.api.response.ClientStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.DetailStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.OrderTypeStatusResponse
import com.afoxplus.orders.data.sources.network.api.response.toEntity

val fakeOrderStatusResponse = OrderStatusResponse(
    id = "6499251936fc949a028e9d23",
    number = "#000008",
    date = "26 Jun 2023, 05:27 AM",
    state = "Pendiente",
    restaurant = "Kitchen",
    orderType = OrderTypeStatusResponse(
        code = "SALON",
        title = "Mesa",
        description = "05"
    ),
    total = "S/ 259.80",
    client = ClientStatusResponse(
        client = "Valentin Mendoza",
        cel = "996443263",
        addressReference = "bello horizonte"
    ),
    detail = arrayListOf(
        DetailStatusResponse(
            productId = "6499215b191a204c25d32516",
            title = "Arroz con Pollo",
            description = "Acompañado de su rica tortilla",
            unitPrice = "S/ 35.80",
            quantity = "1",
            subTotal = "S/ 35.80"
        ),
        DetailStatusResponse(
            productId = "61a5a68c0c327b1d087ccda8",
            title = "Cabrito",
            description = "Acompañado con yuca o mote",
            unitPrice = "S/ 12.00",
            quantity = "1",
            subTotal = "S/ 12.00"
        )
    )
)

val fakeOrderStatus = fakeOrderStatusResponse.toEntity()