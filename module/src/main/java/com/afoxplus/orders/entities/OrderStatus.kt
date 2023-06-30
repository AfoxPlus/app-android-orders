package com.afoxplus.orders.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderStatus(
    val id: String,
    val number: String,
    val date: String,
    val state: String,
    val restaurant: String,
    val orderType: OrderTypeStatus,
    val total: String,
    val client: ClientStatus,
    val detail: List<DetailStatus> = emptyList()
) : Parcelable

@Parcelize
data class OrderTypeStatus(
    val code: String,
    val title: String,
    val description: String = ""
) : Parcelable

@Parcelize
data class ClientStatus(
    val client: String,
    val cel: String,
    val addressReference: String = ""
) : Parcelable

@Parcelize
data class DetailStatus(
    val productId: String,
    val title: String,
    val description: String,
    val unitPrice: String,
    val quantity: String,
    val subTotal: String
) : Parcelable