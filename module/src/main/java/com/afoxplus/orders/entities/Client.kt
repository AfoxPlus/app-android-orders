package com.afoxplus.orders.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val name: String = "",
    val phone: String? = null,
    val addressReference: String? = null
) : Parcelable