package com.afoxplus.orders.di

import com.afoxplus.orders.usecases.actions.*
import com.afoxplus.orders.usecases.actions.AddProductToOrderUseCase
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategyUseCase
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.usecases.actions.FindSaleProductStrategy
import com.afoxplus.products.usecases.actions.HasProductStock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object OrdersUseCaseModule {
    @Provides
    fun provideFindSaleOrderItemStrategy(): FindSaleOrderItemStrategy =
        FindSaleOrderItemStrategyUseCase()

    @Provides
    fun provideAddProductToOrderUseCase(
        orderRepository: OrderRepository,
        hasProductStock: HasProductStock,
        findSaleProductStrategy: FindSaleProductStrategy,
        findSaleOrderItemStrategy: FindSaleOrderItemStrategy
    ): AddProductToOrder =
        AddProductToOrderUseCase(
            orderRepository,
            hasProductStock,
            findSaleProductStrategy,
            findSaleOrderItemStrategy
        )

    @Provides
    fun provideFindProductInOrderUseCase(orderRepository: OrderRepository): FindProductInOrder =
        FindProductInOrderUseCase(orderRepository)
}