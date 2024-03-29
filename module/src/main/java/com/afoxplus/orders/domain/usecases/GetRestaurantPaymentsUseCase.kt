package com.afoxplus.orders.domain.usecases

import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.afoxplus.uikit.objects.vendor.VendorShared
import javax.inject.Inject

internal class GetRestaurantPaymentsUseCase @Inject constructor(private val vendorShared: VendorShared) {
   operator fun invoke(): List<PaymentMethod> {
       val vendor = vendorShared.fetch() ?: return arrayListOf()
        return vendor.paymentMethod
    }
}
