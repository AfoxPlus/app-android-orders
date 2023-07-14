package com.afoxplus.orders.repositories.sources.network.api.response

import com.afoxplus.orders.entities.ClientStatus
import com.afoxplus.orders.entities.DetailStatus
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.entities.OrderTypeStatus
import com.google.gson.annotations.SerializedName

data class OrderStatusResponse(
    val id: String,
    val number: String,
    val date: String,
    val state: String,
    val restaurant: String,
    @SerializedName("order_type")
    val orderType: OrderTypeStatusResponse,
    val total: String,
    val client: ClientStatusResponse,
    val detail: List<DetailStatusResponse> = emptyList()
)

data class OrderTypeStatusResponse(
    val code: String,
    val title: String,
    val description: String? = null
)

data class ClientStatusResponse(
    @SerializedName("name") val client: String,
    val cel: String? = null,
    @SerializedName("address_reference")
    val addressReference: String? = null
)

data class DetailStatusResponse(
    val productId: String,
    val title: String,
    val description: String,
    val unitPrice: String,
    val quantity: String,
    val subTotal: String
)

fun OrderStatusResponse.toEntity(): OrderStatus {
    return OrderStatus(
        id = id,
        number = number,
        date = date,
        state = state,
        restaurant = restaurant,
        orderType = orderType.toEntity(),
        total = total,
        client = client.toEntity(),
        detail = detail.map { it.toEntity() })
}

fun OrderTypeStatusResponse.toEntity(): OrderTypeStatus {
    return OrderTypeStatus(
        code = code,
        title = title,
        description = description ?: ""
    )
}

fun ClientStatusResponse.toEntity(): ClientStatus {
    return ClientStatus(
        client = client,
        cel = cel ?: "",
        addressReference = addressReference ?: ""
    )
}

fun DetailStatusResponse.toEntity(): DetailStatus {
    return DetailStatus(
        productId = productId,
        title = title,
        description = description,
        unitPrice = unitPrice,
        quantity = quantity,
        subTotal = subTotal
    )
}