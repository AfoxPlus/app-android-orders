package com.afoxplus.orders.repositories

import com.afoxplus.orders.data.repositories.OrderStatusRepositorySource
import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.orders.data.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.cross.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderStatusRepositorySourceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockNetwork: OrderNetworkDataSource = mock()

    private lateinit var sutDataSource: OrderStatusRepositorySource

    @Before
    fun setup() {
        sutDataSource = OrderStatusRepositorySource(mockNetwork)
    }

    @Test
    fun `GIVEN list OrderStatus WHEN getOrderStatus THEN return list`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: List<OrderStatus> = mock()
            whenever(mockNetwork.getOrderStatus()).doReturn(mockResponse)

            //WHEN
            val result = sutDataSource.getOrderStatus()

            //THEN
            verify(mockNetwork).getOrderStatus()
            Assert.assertNotNull(result)
            Assert.assertEquals(mockResponse, result)
        }
    }
}