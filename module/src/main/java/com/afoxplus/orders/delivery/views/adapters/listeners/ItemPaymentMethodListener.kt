package com.afoxplus.orders.delivery.views.adapters.listeners

import com.afoxplus.uikit.objects.vendor.PaymentMethod

interface ItemPaymentMethodListener {
    fun onSelected(paymentMethod: PaymentMethod)
}