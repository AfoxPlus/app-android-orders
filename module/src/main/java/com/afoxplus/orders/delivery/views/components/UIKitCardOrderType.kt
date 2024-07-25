package com.afoxplus.orders.delivery.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afoxplus.orders.delivery.views.models.OrderStatusContentUI
import com.afoxplus.orders.delivery.views.models.OrderStatusContentTypeUI
import com.afoxplus.orders.delivery.views.models.TextOrderStatusUI
import com.afoxplus.uikit.designsystem.atoms.UIKitText
import com.afoxplus.uikit.designsystem.foundations.UIKitColorTheme
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import com.afoxplus.uikit.designsystem.foundations.UIKitTypographyTheme

@Composable
internal fun UIKitCardOrderType(
    modifier: Modifier = Modifier,
    orderTypeContent: OrderStatusContentUI
) {
    Card(
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .width(64.dp)
            .height(46.dp),
        border = BorderStroke(UIKitTheme.spacing.spacing01, UIKitColorTheme.gray100),
        shape = RoundedCornerShape(UIKitTheme.spacing.spacing08),
        colors = CardDefaults.cardColors(containerColor = UIKitColorTheme.light01)
    ) {
        UIKitInHouseOrderType(texts = orderTypeContent.getContent())
    }
}

@Composable
internal fun UIKitOrderStatusAmount(
    modifier: Modifier = Modifier,
    orderTypeContent: OrderStatusContentUI
) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .width(intrinsicSize = IntrinsicSize.Max),
        shape = RoundedCornerShape(UIKitTheme.spacing.spacing08),
        colors = CardDefaults.cardColors(containerColor = UIKitColorTheme.blue50)
    ) {
        UIKitInHouseOrderType(
            modifier = Modifier.padding(
                vertical = UIKitTheme.spacing.spacing03,
                horizontal = UIKitTheme.spacing.spacing06
            ),
            texts = orderTypeContent.getContent()
        )
    }
}


@Composable
internal fun UIKitInHouseOrderType(modifier: Modifier = Modifier, texts: List<TextOrderStatusUI>) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        texts.forEach { content ->
            UIKitText(
                text = content.text,
                style = content.style,
                color = content.color
            )
        }
    }
}

@Composable
internal fun OrderStatusRestaurant(modifier: Modifier = Modifier, category: String, name: String) {
    Column(modifier = modifier.fillMaxWidth()) {
        UIKitText(
            text = category,
            style = UIKitTypographyTheme.paragraph02,
            color = UIKitColorTheme.gray700
        )
        UIKitText(
            text = name,
            style = UIKitTypographyTheme.paragraph01Bold,
            color = UIKitColorTheme.secondaryColor
        )
    }
}

@Composable
internal fun OrderStatusChip(modifier: Modifier = Modifier, state: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(UIKitTheme.spacing.spacing04),
        colors = CardDefaults.cardColors(containerColor = UIKitColorTheme.secondaryColor)
    ) {
        UIKitText(
            modifier = Modifier.padding(
                horizontal = UIKitTheme.spacing.spacing08,
                vertical = UIKitTheme.spacing.spacing04
            ),
            text = state,
            style = UIKitTypographyTheme.paragraph01Bold,
            color = UIKitColorTheme.gray50
        )
    }
}

@Composable
internal fun DrawLineDash(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f)
    Canvas(modifier.height(98.dp)) {
        drawLine(
            color = UIKitColorTheme.gray400,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            pathEffect = pathEffect
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun Preview() {
    UIKitTheme {
        Column(modifier = Modifier.padding(UIKitTheme.spacing.spacing16)) {
            OrderStatusChip(state = "Pendiente")
            OrderStatusRestaurant(category = "Cafe y Resto", name = "Kitchen")

            Row(
                modifier = Modifier.padding(UIKitTheme.spacing.spacing16),
                horizontalArrangement = Arrangement.spacedBy(UIKitTheme.spacing.spacing08)
            ) {

                UIKitCardOrderType(
                    orderTypeContent = OrderStatusContentUI(
                        title = "Mesa",
                        description = "05",
                        typeUI = OrderStatusContentTypeUI.IN_HOUSE
                    )
                )
                DrawLineDash()
                UIKitOrderStatusAmount(
                    orderTypeContent = OrderStatusContentUI(
                        title = "Total",
                        description = "S/900.60",
                        typeUI = OrderStatusContentTypeUI.AMOUNT
                    )
                )
            }
        }
    }
}