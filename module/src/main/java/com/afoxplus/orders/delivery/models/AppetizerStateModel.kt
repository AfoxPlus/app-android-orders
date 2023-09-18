package com.afoxplus.orders.delivery.models

import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.uikit.customview.quantitybutton.ButtonEnableType

internal data class AppetizerStateModel(
    val orderAppetizerDetail: OrderAppetizerDetail,
    var isEnable: Boolean,
    var buttonEnableType: ButtonEnableType
)