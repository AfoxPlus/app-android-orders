package com.afoxplus.orders.delivery.flow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import com.afoxplus.orders.delivery.views.activities.AddProductToOrderActivity
import com.afoxplus.orders.delivery.views.activities.OrderPreviewActivity
import com.afoxplus.orders.delivery.views.activities.MarketOrderActivity
import com.afoxplus.orders.delivery.views.activities.ORDER_SUCCESS_MESSAGE
import com.afoxplus.orders.delivery.views.activities.OrderSuccessActivity
import com.afoxplus.orders.delivery.views.fragments.OrderStatusFragment
import com.afoxplus.orders.entities.Order
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import javax.inject.Inject

interface OrderFlow {
    fun goToMarketOrderActivity(activity: Activity)
    fun goToAddProductToOrderActivity(activity: Activity, product: Product)
    fun goToOrderPreviewActivity(activity: Activity, order: Order)
    fun goToOrderSuccessActivity(activity: Activity, message: String)
    fun getStateOrdersFragment(): UIKitBaseFragment

    class OrderFlowImpl @Inject constructor() : OrderFlow {
        override fun goToMarketOrderActivity(activity: Activity) {
            activity.startActivity(MarketOrderActivity.newInstance(activity))
        }

        override fun goToAddProductToOrderActivity(activity: Activity, product: Product) {
            activity.startActivity(Intent(activity, AddProductToOrderActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Product::class.java.name, product)
                })
            })
        }

        override fun goToOrderPreviewActivity(activity: Activity, order: Order) {
            activity.startActivity(Intent(activity, OrderPreviewActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(Order::class.java.name, order)
                })
            })
        }

        override fun goToOrderSuccessActivity(activity: Activity, message: String) {
            activity.startActivity(Intent(activity, OrderSuccessActivity::class.java).apply {
                putExtra(ORDER_SUCCESS_MESSAGE, message)
            })
        }

        override fun getStateOrdersFragment(): UIKitBaseFragment {
            return OrderStatusFragment()
        }

    }
}