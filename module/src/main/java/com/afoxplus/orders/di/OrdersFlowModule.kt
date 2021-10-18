package com.afoxplus.orders.di

import com.afoxplus.orders.delivery.flow.OrderFlow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class OrdersFlowModule {
    @Binds
    abstract fun bindOrderFlow(orderFlowImpl: OrderFlow.OrderFlowImpl): OrderFlow
}