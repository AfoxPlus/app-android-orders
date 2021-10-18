package com.afoxplus.orders.delivery.views.events

import com.afoxplus.orders.entities.Order
import com.afoxplus.uikit.bus.EventBus

interface ProductAddedToCartSuccessfullyEvent : EventBus {
    val order: Order

    private class BuildEvent(override val order: Order) : ProductAddedToCartSuccessfullyEvent
    companion object {
        fun build(order: Order): ProductAddedToCartSuccessfullyEvent = BuildEvent(order)
    }
}