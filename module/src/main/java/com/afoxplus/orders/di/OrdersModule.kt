package com.afoxplus.orders.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object OrdersModule {
    @Provides
    @Singleton
    @Named(OrdersRetrofitModule.PROVIDER_ORDERS_URL)
    fun provideBaseUrl(): String = "http://127.0.0.1:3001/"
}