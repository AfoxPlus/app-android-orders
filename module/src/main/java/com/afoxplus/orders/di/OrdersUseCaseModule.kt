package com.afoxplus.orders.di

import com.afoxplus.orders.usecases.AddProductToOrderUseCase
import com.afoxplus.orders.usecases.FindProductInOrderUseCase
import com.afoxplus.orders.usecases.FindSaleOrderItemStrategyUseCase
import com.afoxplus.orders.usecases.actions.AddProductToOrder
import com.afoxplus.orders.usecases.actions.FindProductInOrder
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategy
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