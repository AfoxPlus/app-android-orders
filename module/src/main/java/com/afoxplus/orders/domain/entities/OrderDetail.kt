package com.afoxplus.orders.domain.entities

import android.os.Parcelable
import com.afoxplus.orders.domain.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.products.entities.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
    val product: Product,
    var quantity: Int,
    var notes: String = "",
    var saleOrderItemStrategy: SaleOrderItemStrategy? = null,
    var appetizers: MutableList<OrderAppetizerDetail> = arrayListOf()
) : Parcelable {

    fun addAppetizerWithQuantity(
        appetizer: Product,
        quantity: Int
    ) {
        appetizers.find { item -> item.product.code == appetizer.code }
            ?.run { this.quantity = quantity } ?: addNewAppetizerWithQuantity(appetizer, quantity)
    }

    private fun addNewAppetizerWithQuantity(
        appetizer: Product,
        quantity: Int
    ) {
        val orderDetail = OrderAppetizerDetail(appetizer, quantity)
        appetizers.add(orderDetail)
    }

    private fun calculateSubTotalWithoutStrategy(): Double {
        return (product.getPriceForSale() * quantity)
    }

    fun calculateSubTotal(): Double {
        return if (saleOrderItemStrategy == null)
            calculateSubTotalWithoutStrategy()
        else
            saleOrderItemStrategy?.calculateSubTotal(this) ?: 0.00
    }
}