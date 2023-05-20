package com.afoxplus.orders.entities

import android.os.Parcelable
import com.afoxplus.orders.entities.bussineslogic.SaleOrderStrategy
import com.afoxplus.products.entities.Product
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Order(
    val date: Date,
    val code: String = "",
    var tableNumber: String = "",
    var clientName: String = "",
    var clientPhoneNumber: String = "",
    private val orderDetails: MutableList<OrderDetail> = mutableListOf(),
    private var saleOrderStrategy: SaleOrderStrategy? = null
) : Parcelable {

    fun addProductWithQuantity(
        product: Product,
        quantity: Int
    ) {
        if (quantity > 0)
            orderDetails.find { item -> item.product.code == product.code }
                ?.run { this.quantity = quantity } ?: addNewProductWithQuantity(product, quantity)
    }

    private fun addNewProductWithQuantity(
        product: Product,
        quantity: Int
    ) {
        val orderDetail = OrderDetail(product, quantity)
        orderDetails.add(orderDetail)
    }

    fun removeItemOrderDetailByProduct(product: Product) {
        val itemProduct = orderDetails.find { item -> item.product.code == product.code }
        orderDetails.remove(itemProduct)
    }

    fun addSaleOrderStrategy(orderStrategy: SaleOrderStrategy) {
        this.saleOrderStrategy = orderStrategy
    }

    fun getOrderDetails(): List<OrderDetail> = orderDetails

    fun getOrderDetailByProduct(product: Product): OrderDetail? =
        orderDetails.find { item -> item.product.code == product.code }

    private fun calculateTotalWithoutStrategy(): Double {
        return orderDetails.sumOf { item -> item.calculateSubTotal() }
    }

    fun calculateTotal(): Double {
        return if (saleOrderStrategy == null)
            calculateTotalWithoutStrategy()
        else saleOrderStrategy?.calculateTotal(this) ?: 0.00
    }

    fun getTotalQuantity(): Int = orderDetails.sumOf { item -> item.quantity }

    fun getTotalWithFormat(): String {
        return "S/ ${String.format("%.2f", calculateTotal())}"
    }

    fun getLabelViewMyOrder(): String =
        "(${getTotalQuantity()}) Ver mi pedido ${getTotalWithFormat()}"

    fun getLabelSendMyOrder(): String =
        "(${getTotalQuantity()}) Enviar mi pedido ${getTotalWithFormat()}"

}