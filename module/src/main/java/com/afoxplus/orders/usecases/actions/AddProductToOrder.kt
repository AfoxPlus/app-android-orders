package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.Flow

fun interface AddProductToOrder {
    suspend operator fun invoke(product: Product, quantity: Int): Flow<Result<Order>>
}