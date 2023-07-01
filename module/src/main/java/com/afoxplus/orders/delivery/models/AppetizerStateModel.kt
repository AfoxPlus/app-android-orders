package com.afoxplus.orders.delivery.models

import com.afoxplus.orders.entities.OrderAppetizerDetail

internal data class AppetizerStateModel(
    val orderAppetizerDetail: OrderAppetizerDetail,
    var isEnable: Boolean
)