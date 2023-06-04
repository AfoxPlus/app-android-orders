package com.afoxplus.orders.demo.global

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.demo.BuildConfig
import javax.inject.Inject

class AppPropertiesDemo @Inject constructor() : AppProperties {
    override fun getDeviceData(): String {
        return "demo - orders"
    }

    override fun isAppDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}