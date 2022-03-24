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
import javax.inject.Singleton

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
    abstract fun bindPlusItemProductToDifferentContextOrder(plusItem: PlusItemProductToDifferentContextOrderUseCase): PlusItemProductToDifferentContextOrder

    @Binds
    abstract fun bindLessItemProductToDifferentContextOrder(lessItem: LessItemProductToDifferentContextOrderUseCase): LessItemProductToDifferentContextOrder

    @Binds
    abstract fun bindSetItemProductInDifferentContextOrder(setItemProduct: SetItemProductInDifferentContextOrderUseCase): SetItemProductInDifferentContextOrder

    @Binds
    abstract fun bindUpdateOrderFromDifferentContext(updateOrderFromDifferentContext: UpdateOrderFromDifferentContextUseCase): UpdateOrderFromDifferentContext
}