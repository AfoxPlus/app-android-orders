package com.afoxplus.orders.delivery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantModel(
    val code: String,
    val name: String? = null,
    val category: String? = null
) : Parcelable