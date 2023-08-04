package com.afoxplus.orders.delivery.views.events

import com.afoxplus.uikit.bus.UIKitEventBus

internal val AddedProductToCurrentOrderSuccessfullyEvent = object : UIKitEventBus {}
internal val GoToNewOrderEvent = object : UIKitEventBus {}