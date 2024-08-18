package com.afoxplus.orders.cross.scan

import android.net.Uri

internal fun getScanDataModelFromUri(data: String): ScanData? {
    try {
        val uri = Uri.parse(data)
        val path = uri.path
        val params = path?.split("/")
        val scanType = params?.get(2)
        if (scanType == "VENDOR") {
            return ScanData(
                tableId = params[3],
                restaurantId = params[4]
            )
        }
        return null
    } catch (ex: Exception) {
        return null
    }
}