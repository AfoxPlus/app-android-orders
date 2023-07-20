package com.afoxplus.orders.demo.di

import com.afoxplus.module.delivery.flow.StartDemoFlow
import com.afoxplus.orders.demo.global.OrderStartDemoFlow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DemoModule {

    @Binds
    fun bindStartDemoFlow(impl: OrderStartDemoFlow): StartDemoFlow
}