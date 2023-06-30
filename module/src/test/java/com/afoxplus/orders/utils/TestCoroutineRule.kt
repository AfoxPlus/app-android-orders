package com.afoxplus.orders.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            println("Apply")
            Dispatchers.setMain(testCoroutineDispatcher)
            base.evaluate()
            //testCoroutineDispatcher.scheduler.advanceUntilIdle()
            Dispatchers.resetMain()
        }
    }

    fun runBlockingTest(block: suspend TestScope.() -> Unit) {
        println("RunBlocking")
        testCoroutineScope.runTest { block() }
    }
}
