package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.products.entities.Product

interface FindSaleOrderItemStrategy {
    suspend operator fun invoke(product: Product): SaleOrderItemStrategy
}

internal class FindSaleOrderItemStrategyUseCase : FindSaleOrderItemStrategy {
    override suspend fun invoke(product: Product): SaleOrderItemStrategy {
        TODO("Not yet implemented")
    }
}