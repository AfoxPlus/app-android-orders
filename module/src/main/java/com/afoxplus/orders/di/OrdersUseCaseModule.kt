package com.afoxplus.orders.di

import com.afoxplus.orders.usecases.AddAppetizerToCurrentOrderUseCase
import com.afoxplus.orders.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.orders.usecases.AddProductsToOrderUseCase
import com.afoxplus.orders.usecases.CalculateSubTotalByProductUseCase
import com.afoxplus.orders.usecases.ClearCurrentOrderUseCase
import com.afoxplus.orders.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.orders.usecases.FetchAppetizerByOrderUseCase
import com.afoxplus.orders.usecases.FindProductInOrderUseCase
import com.afoxplus.orders.usecases.GetCurrentOrderUseCase
import com.afoxplus.orders.usecases.GetRestaurantNameUseCase
import com.afoxplus.orders.usecases.MatchAppetizersByOrderUseCase
import com.afoxplus.orders.usecases.SendOrderUseCase
import com.afoxplus.orders.usecases.actions.AddAppetizerToCurrentOrder
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.AddProductToOrder
import com.afoxplus.orders.usecases.actions.CalculateSubTotalByProduct
import com.afoxplus.orders.usecases.actions.ClearCurrentOrder
import com.afoxplus.orders.usecases.actions.DeleteProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.FetchAppetizerByOrder
import com.afoxplus.orders.usecases.actions.FindProductInOrder
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategy
import com.afoxplus.orders.usecases.actions.FindSaleOrderItemStrategyUseCase
import com.afoxplus.orders.usecases.actions.GetCurrentOrder
import com.afoxplus.orders.usecases.actions.GetRestaurantName
import com.afoxplus.orders.usecases.actions.MatchAppetizersByOrder
import com.afoxplus.orders.usecases.actions.SendOrder
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

    @Binds
    abstract fun bindDeleteProductToCurrentOrder(deleteProductToCurrentOrder: DeleteProductToCurrentOrderUseCase): DeleteProductToCurrentOrder

    @Binds
    abstract fun bindSendOrder(sendOrderUseCase: SendOrderUseCase): SendOrder

    @Binds
    abstract fun bindGetRestaurantName(getRestaurantName: GetRestaurantNameUseCase): GetRestaurantName

    @Binds
    abstract fun bindAddAppetizerToCurrentOrder(addAppetizerToCurrentOrderUseCase: AddAppetizerToCurrentOrderUseCase): AddAppetizerToCurrentOrder

    @Binds
    abstract fun bindFetchAppetizerByOrder(fetchAppetizerByOrderUseCase: FetchAppetizerByOrderUseCase): FetchAppetizerByOrder

    @Binds
    abstract fun bindMatchAppetizersByOrder(matchAppetizersByOrderUseCase: MatchAppetizersByOrderUseCase): MatchAppetizersByOrder
}