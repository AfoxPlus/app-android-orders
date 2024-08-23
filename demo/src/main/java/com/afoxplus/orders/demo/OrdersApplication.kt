package com.afoxplus.orders.demo

import android.app.Application
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.afoxplus.uikit.objects.vendor.Vendor
import com.afoxplus.uikit.objects.vendor.VendorShared
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OrdersApplication : Application() {

    @Inject
    lateinit var vendorShared: VendorShared

    override fun onCreate() {
        super.onCreate()
        vendorShared.save(
            Vendor(
                tableId = "-",
                restaurantId = "648f94bd704db9741d1d2c04",
                additionalInfo = mutableMapOf(
                    "restaurant_name" to "Rest. Pepito sazon!",
                    "restaurant_own_delivery" to false,
                    "restaurant_order_type" to "DELIVERY"
                ),
                paymentMethod = arrayListOf(
                    PaymentMethod("12", "Cúpon de Invitación", true),
                    PaymentMethod("13", "Yape", false),
                    PaymentMethod("14", "Plin", false),
                    PaymentMethod("15", "Credit Card", false)
                )
            )
        )
    }
}