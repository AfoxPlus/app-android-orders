package com.afoxplus.orders.data.sources.network.api.response

import com.google.gson.annotations.SerializedName

internal data class OrderResponse(
    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String
)