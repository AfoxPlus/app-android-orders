package com.afoxplus.orders.delivery.models

import androidx.annotation.StringRes
import com.afoxplus.orders.R

internal data class ButtonStateModel(
    @StringRes val title: Int,
    var enabled: Boolean = false,
    val paramTitle: String = ""
) {

    companion object {
        fun getAddButtonState(paramTitle: String = "", enabled: Boolean = false): ButtonStateModel =
            ButtonStateModel(
                title = R.string.orders_market_add_product,
                paramTitle = if (enabled) paramTitle else "",
                enabled = enabled
            )

        fun getUpdateButtonState(paramTitle: String, enabled: Boolean): ButtonStateModel {
            return ButtonStateModel(
                title = R.string.orders_market_update_product,
                paramTitle = if (enabled) paramTitle else "",
                enabled = enabled
            )
        }

        fun getDeleteButtonState(): ButtonStateModel {
            return ButtonStateModel(
                title = R.string.orders_market_delete_product,
                enabled = true
            )
        }
    }
}