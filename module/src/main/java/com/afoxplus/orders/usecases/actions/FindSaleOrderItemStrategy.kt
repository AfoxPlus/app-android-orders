package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.bussineslogic.SaleOrderItemStrategy
import com.afoxplus.products.entities.Product

interface FindSaleOrderItemStrategy {
    suspend operator fun invoke(product: Product): SaleOrderItemStrategy
}