package com.afoxplus.orders.demo.global

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.demo.BuildConfig
import javax.inject.Inject

class AppPropertiesDemo @Inject constructor() : AppProperties {
    override fun getDeviceData(): String {
        return "demo - orders"
    }

    override fun getUserUUID(): String {
        return "6c4795bd-5a51-46e3-8cf2-3943d53ae271"
    }

    override fun getCurrencyID(): String {
        return "61a18be00b6de1476436de41"
    }

    override fun isAppDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}