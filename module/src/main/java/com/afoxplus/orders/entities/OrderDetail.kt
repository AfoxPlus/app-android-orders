package com.afoxplus.orders.entities

import android.os.Parcelable
import com.afoxplus.orders.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.products.entities.Product
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderDetail(
    val product: Product,
    var quantity: Int,
    private var saleOrderItemStrategy: SaleOrderItemStrategy? = null
) : Parcelable {

    fun addSaleOrderItemStrategy(saleOrderDetailStrategy: SaleOrderItemStrategy?) {
        this.saleOrderItemStrategy = saleOrderDetailStrategy
    }

    fun addQuantity(quantity: Int) {
        this.quantity += quantity
    }

    fun plusItem() {
        this.quantity += 1
    }

    fun lessItems(removeOrderDetail: (orderDetail: OrderDetail) -> Unit) {
        if (this.quantity > 0) {
            this.quantity -= 1
            if (this.quantity == 0)
                removeOrderDetail(this)
        } else removeOrderDetail(this)
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

    fun getQuantityFormat(): String = "$quantity"

}