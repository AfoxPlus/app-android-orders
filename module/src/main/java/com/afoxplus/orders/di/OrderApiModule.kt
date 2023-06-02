package com.afoxplus.orders.di

import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object OrderApiModule {

    @Provides
    fun bindOrderApi(
        @OrderRetrofit retrofit: Retrofit
    ): OrderApiNetwork = retrofit.create(OrderApiNetwork::class.java)
}