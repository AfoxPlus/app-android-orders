package com.afoxplus.orders.delivery.views.events

import com.afoxplus.uikit.bus.EventBus

interface AddedProductToCurrentOrderSuccessfullyEvent : EventBus {
    private class BuildEventProduct: AddedProductToCurrentOrderSuccessfullyEvent
    companion object {
        fun build(): AddedProductToCurrentOrderSuccessfullyEvent = BuildEventProduct()
    }
}