package com.afoxplus.orders.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Rule

abstract class BaseCoroutineTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    val testDispatcherProvider = object : UIKitCoroutineDispatcher {
        override fun getMainDispatcher(): CoroutineDispatcher = testDispatcher
        override fun getIODispatcher(): CoroutineDispatcher = testDispatcher
        override fun getDefaultDispatcher(): CoroutineDispatcher = testDispatcher
    }
}