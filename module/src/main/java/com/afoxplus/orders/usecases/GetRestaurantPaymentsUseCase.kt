package com.afoxplus.orders.usecases

import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.afoxplus.uikit.objects.vendor.VendorShared
import javax.inject.Inject

internal class GetRestaurantPaymentsUseCase @Inject constructor(private val vendorShared: VendorShared) {
    fun invoke(): List<PaymentMethod> {
        return vendorShared.fetch()?.let { vendor ->
            vendor.paymentMethod
        } ?: arrayListOf()
    }
}