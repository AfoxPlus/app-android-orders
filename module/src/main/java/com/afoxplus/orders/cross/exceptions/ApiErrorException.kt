package com.afoxplus.orders.cross.exceptions

import java.io.IOException

internal class ApiErrorException(val contentMessage: ExceptionMessage) : IOException()
internal class OrderBusinessException(val contentMessage: ExceptionMessage) : IOException()
internal data class ExceptionMessage(val value: String, val info: String)