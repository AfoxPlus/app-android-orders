package com.afoxplus.orders.delivery.views.events

import com.afoxplus.uikit.bus.UIKitEventBus

interface AddedProductToCurrentOrderSuccessfullyEvent : UIKitEventBus {
    private class BuildEventProduct: AddedProductToCurrentOrderSuccessfullyEvent
    companion object {
        fun build(): AddedProductToCurrentOrderSuccessfullyEvent = BuildEventProduct()
    }
}