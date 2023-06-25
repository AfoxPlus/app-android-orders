package com.afoxplus.orders.demo

import android.app.Application
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
                tableId = "09",
                restaurantId = "648f94bd704db9741d1d2c04",
                additionalInfo = mutableMapOf(
                    "restaurant_name" to "Rest. Pepito sazon!",
                    "restaurant_own_delivery" to false
                )
            )
        )
    }
}