package com.afoxplus.orders.di

import com.afoxplus.orders.repositories.OrderRepositorySource
import com.afoxplus.orders.usecases.repositories.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OrdersRepositoryModule {
    @Binds
    abstract fun provideOrderRepository(orderRepositorySource: OrderRepositorySource): OrderRepository
}