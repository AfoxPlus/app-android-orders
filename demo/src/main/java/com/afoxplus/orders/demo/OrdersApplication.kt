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
        vendorShared.save(Vendor(tableId = "01", restaurantId = "61a19c440b6de1476436de4a"))
    }
}