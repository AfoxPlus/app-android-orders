package com.afoxplus.orders.repositories.exceptions

import java.io.IOException

class ApiErrorException(val title: String, val errorMessage: String) : IOException()