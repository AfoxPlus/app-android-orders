package com.afoxplus.orders.delivery.views.models

import com.afoxplus.uikit.designsystem.foundations.UIKitColorTheme
import com.afoxplus.uikit.designsystem.foundations.UIKitTypographyTheme

internal data class OrderStatusContentUI(
    val title: String,
    val description: String? = null,
    val typeUI: OrderStatusContentTypeUI
) {

    fun getContent(): List<TextOrderStatusUI> {
        return when (typeUI) {
            OrderStatusContentTypeUI.DELIVERY -> {
                listOf(
                    TextOrderStatusUI(
                        text = title,
                        style = UIKitTypographyTheme.paragraph02Bold,
                        color = UIKitColorTheme.rose600
                    )
                )
            }

            OrderStatusContentTypeUI.IN_HOUSE -> listOf(
                TextOrderStatusUI(
                    text = title,
                    style = UIKitTypographyTheme.paragraph02Bold,
                    color = UIKitColorTheme.gray700
                ),
                TextOrderStatusUI(
                    text = description.toString(),
                    style = UIKitTypographyTheme.header03Bold,
                    color = UIKitColorTheme.secondaryColor
                )
            )

            OrderStatusContentTypeUI.AMOUNT -> listOf(
                TextOrderStatusUI(
                    text = title,
                    style = UIKitTypographyTheme.paragraph02Bold,
                    color = UIKitColorTheme.blue800
                ),
                TextOrderStatusUI(
                    text = description.toString(),
                    style = UIKitTypographyTheme.header03Bold,
                    color = UIKitColorTheme.blue800
                )
            )

            OrderStatusContentTypeUI.STORE_PICKUP -> {
                listOf(
                    TextOrderStatusUI(
                        text = title,
                        style = UIKitTypographyTheme.paragraph02Bold,
                        color = UIKitColorTheme.secondaryColor
                    )
                )
            }
        }
    }
}