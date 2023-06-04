package com.afoxplus.orders.di

import com.afoxplus.network.api.RetrofitGenerator
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object OrderApiModule {

    @Provides
    fun bindOrderApi(
        retrofitGenerator: RetrofitGenerator
    ): OrderApiNetwork = retrofitGenerator.createRetrofit(OrderApiNetwork::class.java)
}