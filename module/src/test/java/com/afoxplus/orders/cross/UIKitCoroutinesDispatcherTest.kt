package com.afoxplus.orders.cross

import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UIKitCoroutinesDispatcherTest : UIKitCoroutineDispatcher {
    override fun getDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun getIODispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun getMainDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}