package com.afoxplus.orders.di

import com.afoxplus.orders.data.sources.local.OrderLocalDataSource
import com.afoxplus.orders.data.sources.local.cache.OrderLocalCache
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {
    @Binds
    abstract fun provideOderLocalDataSource(localCache: OrderLocalCache): OrderLocalDataSource
}