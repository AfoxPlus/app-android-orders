package com.afoxplus.orders.repositories

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.data.repositories.OrderRepositorySource
import com.afoxplus.orders.data.sources.local.OrderLocalDataSource
import com.afoxplus.orders.data.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.common.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderRepositorySourceTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockLocalSource: OrderLocalDataSource = mock()
    private val mockNetworkSource: OrderNetworkDataSource = mock()

    private val sutDataSource: OrderRepositorySource by lazy {
        OrderRepositorySource(
            mockLocalSource,
            mockNetworkSource
        )
    }

    @Test
    fun `WHEN clearCurrentOrder THEN clear the current order from local source`() {
        testCoroutineRule.runBlockingTest {
            //WHEN
            val result = sutDataSource.clearCurrentOrder()

            //THEN
            verify(mockLocalSource).clearCurrentOrder()
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `WHEN getCurrentOrder THEN return the order from local source`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockResponse: SharedFlow<Order> = mock()
            whenever(mockLocalSource.getCurrentOrder()).doReturn(mockResponse)

            //WHEN
            val result = sutDataSource.getCurrentOrder()

            //THEN
            verify(mockLocalSource).getCurrentOrder()
            Assert.assertNotNull(result)
            Assert.assertEquals(mockResponse, result)
        }
    }

    @Test
    fun `GIVEN a product WHEN deleteProductToCurrentOrder THEN delete the product from local source`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()

            //WHEN
            val result = sutDataSource.deleteProductToCurrentOrder(productMock)

            //THEN
            verify(mockLocalSource).deleteProductToCurrentOrder(productMock)
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN an order WHEN sendOrder THEN send the order to the remote source and clear it from the local one`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val orderMock: Order = mock()
            val responseMock: ResultState<String> = ResultState.Success("")
            whenever(mockNetworkSource.sendOrder(orderMock)).doReturn(responseMock)

            //WHEN
            val result = sutDataSource.sendOrder(orderMock)

            //THEN
            verify(mockNetworkSource).sendOrder(orderMock)
            verify(mockLocalSource).clearCurrentOrder()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, responseMock)
        }
    }

    @Test
    fun `GIVEN a product and the quantity WHEN addOrUpdateProductToCurrentOrder THEN add or update them on the local source`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2

            //WHEN
            val result = sutDataSource.addOrUpdateProductToCurrentOrder(quantityMock, productMock)

            //THEN
            verify(mockLocalSource).addOrUpdateProductToCurrentOrder(quantityMock, productMock)
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN a product WHEN findProductInCurrentOrder THEN return it from the current order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val responseMock: OrderDetail = mock()
            whenever(mockLocalSource.findProductInOrder(productMock)).doReturn(responseMock)

            //WHEN
            val result = sutDataSource.findProductInCurrentOrder(productMock)

            //THEN
            verify(mockLocalSource).findProductInOrder(productMock)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, responseMock)
        }
    }

    @Test
    fun `GIVEN an appetizer, a product and the quantity WHEN addOrUpdateAppetizerToCurrentOrder THEN add or update them in the current local order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val appetizerMock: Product = mock()
            val quantityMock = 2

            //WHEN
            val result = sutDataSource.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            //THEN
            verify(mockLocalSource).addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN a product WHEN fetchAppetizersByProduct THEN return a list of appetizer from the current local order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val responseMock: List<OrderAppetizerDetail> = mock()
            whenever(mockLocalSource.fetchAppetizersByProduct(productMock)).doReturn(responseMock)

            //WHEN
            val result = sutDataSource.fetchAppetizersByProduct(productMock)

            //THEN
            verify(mockLocalSource).fetchAppetizersByProduct(productMock)
            Assert.assertNotNull(result)
            Assert.assertEquals(result, responseMock)
        }
    }

    @Test
    fun `GIVEN a product WHEN clearAppetizersByProduct THEN clear the appetizer from the current local order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()

            //WHEN
            val result = sutDataSource.clearAppetizersByProduct(productMock)

            //THEN
            verify(mockLocalSource).clearAppetizersByProduct(productMock)
            Assert.assertNotNull(result)
        }
    }
}