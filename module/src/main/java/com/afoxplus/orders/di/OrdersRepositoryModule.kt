package com.afoxplus.orders.di

import com.afoxplus.orders.repositories.OrderRepositorySource
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.orders.usecases.repositories.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object OrdersRepositoryModule {
    @Provides
    fun provideOrderRepository(
        orderLocalDataSource: OrderLocalDataSource
    ): OrderRepository = OrderRepositorySource(orderLocalDataSource)
}