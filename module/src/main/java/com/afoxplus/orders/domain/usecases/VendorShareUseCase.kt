package com.afoxplus.orders.domain.usecases

import com.afoxplus.uikit.objects.vendor.VendorShared
import javax.inject.Inject

internal class VendorShareUseCase @Inject constructor(private val vendorShared: VendorShared) {
    fun getRestaurantName(): String {
        val vendor = vendorShared.fetch() ?: return ""
        return vendor.additionalInfo[PARAM_RESTAURANT_NAME].toString()
    }

     fun getGuestName(): String? {
         return try {
             vendorShared.fetch()?.let { item -> item.additionalInfo[PARAM_INVITATION_GUEST].toString() }
         }catch (ex: Exception) {
             null
         }
    }

    companion object {
        private const val PARAM_RESTAURANT_NAME = "restaurant_name"
        private const val PARAM_INVITATION_GUEST = "invitation_guest"
    }
}