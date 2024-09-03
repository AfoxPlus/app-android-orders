package com.afoxplus.orders.domain.entities

import android.os.Parcelable
import com.afoxplus.orders.domain.entities.bussineslogic.SaleOrderStrategy
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import kotlinx.parcelize.Parcelize

@Parcelize
class Order(
    val code: String = "",
    var orderType: OrderType,
    var restaurantId: String = "",
    var client: Client? = null,
    var paymentMethod: PaymentMethod? = null,
    private val orderDetails: MutableList<OrderDetail> = mutableListOf(),
    private var saleOrderStrategy: SaleOrderStrategy? = null
) : Parcelable {

    fun addUpdateOrDeleteProductWithQuantity(
        product: Product,
        quantity: Int,
        notes: String
    ) {
        if (quantity > 0)
            orderDetails.find { item -> item.product.code == product.code }
                ?.run {
                    this.quantity = quantity
                    this.notes = notes
                } ?: addNewProductWithQuantity(product, quantity, notes)
        if (quantity == 0)
            removeItemOrderDetailByProduct(product)
    }

    private fun addNewProductWithQuantity(
        product: Product,
        quantity: Int,
        notes: String
    ) {
        val orderDetail = OrderDetail(product, quantity, notes)
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

    fun isValidOrderType(): Boolean {
        return !(orderType == OrderType.Local && orderType.description == "-")
    }

    fun getLabelViewMyOrder(): String =
        "(${getTotalQuantity()}) Ver mi pedido ${getTotalWithFormat()}"

    fun getLabelSendMyOrder(): String =
        "(${getTotalQuantity()}) Enviar mi pedido ${getTotalWithFormat()}"

    fun isOrderEmpty(): Boolean = getOrderDetails().isEmpty()
}