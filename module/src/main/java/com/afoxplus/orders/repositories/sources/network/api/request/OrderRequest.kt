package com.afoxplus.orders.repositories.sources.network.api.request

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.google.gson.annotations.SerializedName

internal data class OrderRequest(
    @SerializedName("client") val client: ClientRequest,
    @SerializedName("date") val date: String,
    @SerializedName("table_number") val tableNumber: String,
    @SerializedName("restaurant_id") val restaurantId: String,
    @SerializedName("detail") val detail: List<OrderDetailRequest>,
    @SerializedName("total") val total: Double
) {

    companion object {
        fun getOrderRequest(order: Order): OrderRequest {
            return OrderRequest(
                client = ClientRequest(
                    name = order.clientName,
                    cellphone = order.clientPhoneNumber
                ),
                tableNumber = order.tableNumber,
                restaurantId = order.restaurantId,
                total = order.calculateTotal(),
                date = order.date.toString(),
                detail = order.getOrderDetails()
                    .map { item -> OrderDetailRequest.getOrderDetailRequest(item) }
            )
        }
    }
}

internal data class ClientRequest(val name: String, val cellphone: String)

internal data class OrderDetailRequest(
    @SerializedName("product_id") val productId: String,
    @SerializedName("description") val description: String,
    @SerializedName("unit_price") val unitPrice: Double,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("sub_total") val subTotal: Double,
    @SerializedName("currency_code") val currencyCode: String
) {

    companion object {
        fun getOrderDetailRequest(orderDetail: OrderDetail): OrderDetailRequest {
            return OrderDetailRequest(
                productId = orderDetail.product.code,
                description = orderDetail.product.description,
                unitPrice = orderDetail.product.getPriceForSale(),
                quantity = orderDetail.quantity,
                subTotal = orderDetail.calculateSubTotal(),
                currencyCode = orderDetail.product.currency.code
            )
        }
    }
}