package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.products.entities.Product
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para sumar o restar un producto desde el detalle del pedido
 */
fun interface AddProductToOrder {
    suspend operator fun invoke(product: Product, quantity: Int): Flow<Result<Order>>
}
