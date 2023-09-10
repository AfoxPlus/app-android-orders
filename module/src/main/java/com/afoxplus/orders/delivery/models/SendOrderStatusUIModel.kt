package com.afoxplus.orders.delivery.models

internal sealed class SendOrderStatusUIModel {
    data class Success(val message: String) : SendOrderStatusUIModel()
    data class Error(val exception: Exception) : SendOrderStatusUIModel()
}
