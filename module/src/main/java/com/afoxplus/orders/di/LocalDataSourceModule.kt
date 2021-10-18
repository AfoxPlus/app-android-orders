package com.afoxplus.orders.di

import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.orders.repositories.sources.local.cache.OrderLocalCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalDataSourceModule {
    @Singleton
    @Provides
    fun provideOderLocalDataSource(): OrderLocalDataSource = OrderLocalCache()
}