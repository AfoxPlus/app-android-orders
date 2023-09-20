package com.afoxplus.orders.data.sources.network.api.response

import com.google.gson.annotations.SerializedName


data class OrderBaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: OrderMessageResponse,
    @SerializedName("payload") val payload: T
)

data class OrderMessageResponse(
    @SerializedName("value") val value: String,
    @SerializedName("info") val info: String? = null,
    @SerializedName("meta_data") val metaData: Any? = null
)