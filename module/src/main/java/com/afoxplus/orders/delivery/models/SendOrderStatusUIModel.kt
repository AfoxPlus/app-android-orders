package com.afoxplus.orders.delivery.models

sealed class SendOrderStatusUIModel {
    data class Success(val message: String) : SendOrderStatusUIModel()
    data class Error(val title: String, val message: String) : SendOrderStatusUIModel()
}