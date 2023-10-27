package com.afoxplus.orders.di

import com.afoxplus.orders.data.repositories.OrderRepositorySource
import com.afoxplus.orders.data.repositories.OrderStatusRepositorySource
import com.afoxplus.orders.domain.repositories.OrderRepository
import com.afoxplus.orders.domain.repositories.OrderStatusRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OrdersRepositoryModule {
    @Binds
    abstract fun provideOrderRepository(orderRepositorySource: OrderRepositorySource): OrderRepository

    @Binds
    abstract fun provideOrderStatusRepository(
        repositorySource: OrderStatusRepositorySource
    ): OrderStatusRepository

}