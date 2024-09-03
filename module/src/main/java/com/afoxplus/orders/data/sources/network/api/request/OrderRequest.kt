package com.afoxplus.orders.data.sources.network.api.request

import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.google.gson.annotations.SerializedName

internal data class OrderRequest(
    @SerializedName("client") val client: ClientRequest,
    @SerializedName("order_type") val orderType: OrderTypeRequest,
    @SerializedName("restaurant_id") val restaurantId: String,
    @SerializedName("detail") val detail: List<OrderDetailRequest>,
    @SerializedName("total") val total: Double,
    @SerializedName("payment_method") val paymentMethod: String
) {

    companion object {
        fun getOrderRequest(order: Order): OrderRequest {
            return OrderRequest(
                client = ClientRequest(
                    name = order.client?.name ?: "",
                    cellphone = order.client?.phone,
                    addressReference = order.client?.addressReference
                ),
                orderType = OrderTypeRequest(
                    code = order.orderType.code,
                    title = order.orderType.title,
                    description = order.orderType.description
                ),
                restaurantId = order.restaurantId,
                total = order.calculateTotal(),
                detail = order.getOrderDetails()
                    .map { item -> OrderDetailRequest.getOrderDetailRequest(item) },
                paymentMethod = order.paymentMethod?.name ?: ""
            )
        }
    }
}

internal data class ClientRequest(
    @SerializedName("name") val name: String,
    @SerializedName("cel") val cellphone: String? = null,
    @SerializedName("address_reference") val addressReference: String? = null
)

internal data class OrderTypeRequest(
    @SerializedName("code") val code: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null
)

internal data class OrderDetailRequest(
    @SerializedName("product_type") val type: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("unit_price") val unitPrice: Double,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("note") val notes: String,
    @SerializedName("sub_total") val subTotal: Double,
    @SerializedName("sub_detail") val appetizers: List<OrderAppetizerRequest>
) {

    companion object {
        fun getOrderDetailRequest(orderDetail: OrderDetail): OrderDetailRequest {
            return OrderDetailRequest(
                productId = orderDetail.product.code,
                title = orderDetail.product.name,
                description = orderDetail.product.description,
                unitPrice = orderDetail.product.getPriceForSale(),
                notes = orderDetail.notes,
                quantity = orderDetail.quantity,
                subTotal = orderDetail.calculateSubTotal(),
                type = orderDetail.product.productType.code,
                appetizers = orderDetail.appetizers.map {
                    OrderAppetizerRequest.getOrderAppetizerRequest(it)
                }
            )
        }
    }
}

internal data class OrderAppetizerRequest(
    @SerializedName("product_id") val productId: String,
    @SerializedName("title") val title: String,
    @SerializedName("quantity") val quantity: String,
) {
    companion object {
        fun getOrderAppetizerRequest(orderAppetizerDetail: OrderAppetizerDetail): OrderAppetizerRequest {
            return OrderAppetizerRequest(
                productId = orderAppetizerDetail.product.code,
                title = orderAppetizerDetail.product.name,
                quantity = orderAppetizerDetail.quantity.toString()
            )
        }
    }
}