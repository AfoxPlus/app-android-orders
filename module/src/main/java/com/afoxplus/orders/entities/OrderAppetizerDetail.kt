package com.afoxplus.orders.entities

import android.os.Parcelable
import com.afoxplus.products.entities.Product
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderAppetizerDetail(val product: Product, var quantity: Int) : Parcelable