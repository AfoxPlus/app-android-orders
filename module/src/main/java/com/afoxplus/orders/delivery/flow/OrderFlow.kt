package com.afoxplus.orders.delivery.flow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.afoxplus.orders.delivery.views.activities.AddCartProductActivity
import com.afoxplus.orders.delivery.views.activities.CartProductsActivity
import com.afoxplus.orders.delivery.views.activities.MarketOrderActivity
import com.afoxplus.orders.entities.Order
import com.afoxplus.products.entities.Product
import javax.inject.Inject

interface OrderFlow {
    fun goToMarketOrder(activity: Activity)
    fun goToAddCartProduct(activity: Activity, product: Product)
    fun goToCartProducts(activity: Activity, order: Order)

    class OrderFlowImpl @Inject constructor() : OrderFlow {
        override fun goToMarketOrder(activity: Activity) {
            Intent(activity, MarketOrderActivity::class.java).run { activity.startActivity(this) }
        }

        override fun goToAddCartProduct(activity: Activity, product: Product) {
            activity.startActivity(Intent(activity, AddCartProductActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Product::class.java.name, product)
                })
            })
        }

        override fun goToCartProducts(activity: Activity, order: Order) {
            activity.startActivity(Intent(activity, CartProductsActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Order::class.java.name, order)
                })
            })
        }

    }
}