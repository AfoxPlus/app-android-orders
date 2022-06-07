package com.afoxplus.orders.di

import com.afoxplus.orders.usecases.actions.*
import com.afoxplus.orders.usecases.actions.AddProductsToOrderUseCase
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategyUseCase
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.usecases.actions.FindSaleProductStrategy
import com.afoxplus.products.usecases.actions.HasProductStock
import dagger.Binds
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
        AddProductsToOrderUseCase(
            orderRepository,
            hasProductStock,
            findSaleProductStrategy,
            findSaleOrderItemStrategy
        )

    @Provides
    fun provideFindProductInOrderUseCase(orderRepository: OrderRepository): FindProductInOrder =
        FindProductInOrderUseCase(orderRepository)

}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OrdersUseCaseInternalModule {

    @Binds
    abstract fun bindUpdateOrderFromDifferentContext(updateOrderFromDifferentContext: AddOrUpdateProductToCurrentOrderUseCase): AddOrUpdateProductToCurrentOrder

    @Binds
    abstract fun bindClearLocalOrder(clearLocalOrder: ClearCurrentOrderUseCase): ClearCurrentOrder

    @Binds
    abstract fun bindCalculateSubTotalByProduct(calculateSubTotalByProduct: CalculateSubTotalByProductUseCase): CalculateSubTotalByProduct

    @Binds
    abstract fun bindGetCurrentOrderFlow(getCurrentOrderFlow: GetCurrentOrderUseCase): GetCurrentOrder
}