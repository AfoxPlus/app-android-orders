package com.afoxplus.orders.di

import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.flow.OrderFlowImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal fun interface OrdersFlowModule {
    @Binds
    fun bindOrderFlow(orderFlowImpl: OrderFlowImpl): OrderFlow
}