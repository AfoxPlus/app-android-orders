package com.afoxplus.orders.delivery.views.events

import com.afoxplus.uikit.bus.UIKitEventBus

interface GoToNewOrderEvent : UIKitEventBus {

    private class BuildEventProduct: GoToNewOrderEvent
    companion object {
        fun build(): GoToNewOrderEvent = BuildEventProduct()
    }
}