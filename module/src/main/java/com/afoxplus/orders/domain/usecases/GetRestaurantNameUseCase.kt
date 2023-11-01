package com.afoxplus.orders.domain.usecases

import com.afoxplus.uikit.objects.vendor.VendorShared
import javax.inject.Inject

internal class GetRestaurantNameUseCase @Inject constructor(private val vendorShared: VendorShared) {
    operator fun invoke(): String {
        val vendor = vendorShared.fetch() ?: return ""
        return vendor.additionalInfo[PARAM_RESTAURANT_NAME].toString()
    }

    companion object {
        private const val PARAM_RESTAURANT_NAME = "restaurant_name"
    }
}