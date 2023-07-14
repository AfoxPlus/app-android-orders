package com.afoxplus.orders.di

import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.service.OrderNetworkService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkDataSourceModule {

    @Binds
    abstract fun bindOrderNetworkDataSource(orderNetworkService: OrderNetworkService): OrderNetworkDataSource
}