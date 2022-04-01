package com.afoxplus.orders.entities

import android.os.Parcelable
import com.afoxplus.orders.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.orders.entities.bussineslogic.SaleOrderStrategy
import com.afoxplus.products.entities.Product
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Order(
    val date: Date,
    val code: String = "",
    private val orderDetails: MutableList<OrderDetail> = mutableListOf(),
    private var saleOrderStrategy: SaleOrderStrategy? = null
) : Parcelable {

    fun addProduct(
        product: Product,
        quantity: Int,
        saleOrderItemStrategy: SaleOrderItemStrategy? = null
    ) {
        val itemsCart = orderDetails.filter { item -> item.product.code == product.code }
        if (itemsCart.isNotEmpty()) {
            orderDetails.find { item -> item.product.code == product.code }?.addQuantity(quantity)
        } else {
            val orderDetail = OrderDetail(product, quantity)
            orderDetail.addSaleOrderItemStrategy(saleOrderItemStrategy)
            orderDetails.add(orderDetail)
        }
    }

    fun plusItemProduct(product: Product) {
        val itemsCart = orderDetails.filter { item -> item.product.code == product.code }
        if (itemsCart.isNotEmpty()) {
            orderDetails.find { item -> item.product.code == product.code }?.plusItem()
        } else {
            val orderDetail = OrderDetail(product, 1)
            orderDetails.add(orderDetail)
        }
    }

    fun lessItemProduct(product: Product) {
        val itemsCart = orderDetails.filter { item -> item.product.code == product.code }
        if (itemsCart.isNotEmpty()) {
            orderDetails.find { item -> item.product.code == product.code }
                ?.lessItems(::removeItemOrderDetail)
        }
    }

    private fun removeItemOrderDetail(orderDetail: OrderDetail) {
        orderDetails.remove(orderDetail)
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
        "${getTotalWithFormat()} Enviar mi pedido"
}