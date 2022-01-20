package com.afoxplus.orders.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderGsonConverterFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderHttpLoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderBaseURL