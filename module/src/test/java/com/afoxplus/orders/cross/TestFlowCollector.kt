package com.afoxplus.orders.cross

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * [TestFlowCollector] collects emitted values which allows making assertions about them.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestFlowCollector<T>(
    flow: Flow<T>,
    scope: TestScope
) {
    private var job: Job? = null
    private val values = mutableListOf<T>()

    init {
        job = scope.launch(UnconfinedTestDispatcher(scope.testScheduler)) {
            flow.toCollection(values)
        }
    }

    /**
     * Returns the collected values.
     */
    fun values(): List<T> {
        return values
    }

    /**
     * Cancels the values collection.
     */
    suspend fun cancel() {
        job?.cancelAndJoin()
        job = null
    }
}

/**
 * Creates a [TestFlowCollector] that collects values from this flow.
 * The returned [TestFlowCollector] must be explicitly canceled.
 *
 * Example:
 * ```
 * runTest {
 *     val testCollector = flowOf(1, 2).testIn(this)
 *     testScheduler.advanceUntilIdle()
 *     testCollector.cancel()
 *     assertEquals(listOf(1, 2), testCollector.values())
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.testIn(testScope: TestScope): TestFlowCollector<T> {
    return TestFlowCollector(this, testScope)
}