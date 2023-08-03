package com.afoxplus.orders.usecases

import com.afoxplus.orders.usecases.actions.GetRestaurantName
import com.afoxplus.uikit.objects.vendor.VendorShared
import javax.inject.Inject

internal class GetRestaurantNameUseCase @Inject constructor(private val vendorShared: VendorShared) :
    GetRestaurantName {
    override fun invoke(): String {
        return vendorShared.fetch()?.let { vendor ->
            vendor.additionalInfo[PARAM_RESTAURANT_NAME].toString()
        } ?: ""
    }

    companion object {
        private const val PARAM_RESTAURANT_NAME = "restaurant_name"
    }
}