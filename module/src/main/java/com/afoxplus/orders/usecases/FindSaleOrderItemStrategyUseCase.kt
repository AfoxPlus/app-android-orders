package com.afoxplus.orders.usecases

import com.afoxplus.orders.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategy
import com.afoxplus.products.entities.Product


internal class FindSaleOrderItemStrategyUseCase : FindSaleOrderItemStrategy {
    override suspend fun invoke(product: Product): SaleOrderItemStrategy {
        TODO("Not yet implemented")
    }
}