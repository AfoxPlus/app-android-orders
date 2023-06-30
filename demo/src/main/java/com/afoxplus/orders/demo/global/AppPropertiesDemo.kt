package com.afoxplus.orders.demo.global

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.demo.BuildConfig
import javax.inject.Inject

class AppPropertiesDemo @Inject constructor() : AppProperties {
    override fun getCurrencyID(): String {
        TODO("Not yet implemented")
    }

    override fun getDeviceData(): String {
        return "demo - orders"
    }

    override fun getUserUUID(): String {
        TODO("Not yet implemented")
    }

    override fun isAppDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}