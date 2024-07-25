package com.afoxplus.orders.delivery.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.afoxplus.orders.delivery.views.models.OrderStatusContentTypeUI
import com.afoxplus.orders.delivery.views.models.OrderStatusContentUI
import com.afoxplus.uikit.designsystem.foundations.UIKitColorTheme
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme

@Composable
internal fun ItemOrderStatus(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(UIKitTheme.spacing.spacing02, UIKitColorTheme.gray200),
        shape = RoundedCornerShape(UIKitTheme.spacing.spacing08),
        colors = CardDefaults.cardColors(containerColor = UIKitColorTheme.light01)
    ) {
        Row(modifier = Modifier.padding(UIKitTheme.spacing.spacing12)) {
            Column(modifier = Modifier.weight(1f)) {
                OrderStatusChip(state = "Pendiente")
                OrderStatusRestaurant(category = "Cafe y Resto", name = "Kitchen")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(UIKitTheme.spacing.spacing08)
                ) {

                    UIKitCardOrderType(
                        orderTypeContent = OrderStatusContentUI(
                            title = "Mesa",
                            description = "05",
                            typeUI = OrderStatusContentTypeUI.IN_HOUSE
                        )
                    )
                    UIKitOrderStatusAmount(
                        orderTypeContent = OrderStatusContentUI(
                            title = "Total",
                            description = "S/900.60",
                            typeUI = OrderStatusContentTypeUI.AMOUNT
                        )
                    )
                }
            }
            //DrawLineDash()

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComponent() {
    UIKitTheme {
        ItemOrderStatus()
    }
}