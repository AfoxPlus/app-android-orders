package com.afoxplus.orders.delivery.flow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.afoxplus.orders.delivery.views.activities.AddProductToCartActivity
import com.afoxplus.orders.delivery.views.activities.ShopCartActivity
import com.afoxplus.orders.delivery.views.activities.MarketOrderActivity
import com.afoxplus.orders.entities.Order
import com.afoxplus.products.entities.Product
import javax.inject.Inject

interface OrderFlow {
    fun goToMarketOrderActivity(activity: Activity)
    fun goToAddProductToCardActivity(activity: Activity, product: Product)
    fun goToCartProducts(activity: Activity, order: Order)

    class OrderFlowImpl @Inject constructor() : OrderFlow {
        override fun goToMarketOrderActivity(activity: Activity) {
            Intent(activity, MarketOrderActivity::class.java).run { activity.startActivity(this) }
        }

        override fun goToAddProductToCardActivity(activity: Activity, product: Product) {
            activity.startActivity(Intent(activity, AddProductToCartActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Product::class.java.name, product)
                })
            })
        }

        override fun goToCartProducts(activity: Activity, order: Order) {
            activity.startActivity(Intent(activity, ShopCartActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Order::class.java.name, order)
                })
            })
        }

    }
}