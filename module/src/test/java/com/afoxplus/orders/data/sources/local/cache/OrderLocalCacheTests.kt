package com.afoxplus.orders.data.sources.local.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.testIn
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.MenuDish
import com.afoxplus.uikit.objects.vendor.Vendor
import com.afoxplus.uikit.objects.vendor.VendorShared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderLocalCacheTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockVendorShared: VendorShared = mock()
    private val sutOrderLocalCache: OrderLocalCache by lazy { OrderLocalCache(mockVendorShared) }

    @Test
    fun `GIVEN a product and the quantity WHEN addOrUpdateProductToCurrentOrder THEN add or update them on the local source for delivery`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2
            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            //WHEN
            val result =
                sutOrderLocalCache.addOrUpdateProductToCurrentOrder(quantityMock, productMock)

            //THEN
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN a product and the quantity WHEN addOrUpdateProductToCurrentOrder and there is an order THEN add or update them on the local source`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2
            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            createOrder(productMock)

            //WHEN
            val result =
                sutOrderLocalCache.addOrUpdateProductToCurrentOrder(quantityMock, productMock)

            //THEN
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN a product and the quantity WHEN addOrUpdateProductToCurrentOrder THEN add or update them on the local source for local`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2
            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to false),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            //WHEN
            val result =
                sutOrderLocalCache.addOrUpdateProductToCurrentOrder(quantityMock, productMock)

            //THEN
            Assert.assertNotNull(result)
        }
    }

    @Test
    fun `GIVEN a product and the quantity WHEN addOrUpdateProductToCurrentOrder and the vendor is null THEN  return an exception`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock: Product = mock()
            val quantityMock = 2
            val mockErrorMessage = "No found DeliveryType"

            try {
                whenever(mockVendorShared.fetch()).doReturn(null)

                //WHEN
                sutOrderLocalCache.addOrUpdateProductToCurrentOrder(quantityMock, productMock)

            } catch (ex: Exception) {
                //THEN
                Assert.assertNotNull(ex.message == mockErrorMessage)
            }

        }
    }


    @Test
    fun `WHEN clearCurrentOrder THEN clear the order from cache`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.clearCurrentOrder()

            //THEN
            Assert.assertNotNull(result)

            val sharedFlow = sutOrderLocalCache.getCurrentOrder()
            val testCollector = sharedFlow.testIn(this)
            testCollector.cancel()
            val orderResponse = testCollector.values().first()
            Assert.assertEquals(orderResponse, null)
        }
    }

    @Test
    fun `GIVEN a product WHEN findProductInOrder THEN return the orderDetail related to that product`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.findProductInOrder(productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(result?.product?.code, productMock.code)

        }
    }

    @Test
    fun `GIVEN a product WHEN findProductInOrder the product does not exist on the order THEN return null`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            val productToFindMock: Product = mock()

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.findProductInOrder(productToFindMock)

            //THEN
            Assert.assertEquals(result, null)
        }
    }

    @Test
    fun `GIVEN a product WHEN findProductInOrder and the order does not exist THEN return null`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productToFindMock: Product = mock()

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)

            //WHEN
            val result = sutOrderLocalCache.findProductInOrder(productToFindMock)

            //THEN
            Assert.assertEquals(result, null)
        }
    }

    @Test
    fun `GIVEN a product WHEN deleteProductToCurrentOrder THEN return an order with empty order details`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.deleteProductToCurrentOrder(productMock)

            val sharedFlow = sutOrderLocalCache.getCurrentOrder()
            val testCollector = sharedFlow.testIn(this)
            testCollector.cancel()
            val orderResponse = testCollector.values().first()

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(orderResponse?.getOrderDetails()?.size, 0)
        }
    }

    @Test
    fun `GIVEN a product WHEN deleteProductToCurrentOrder and the order does not exist THEN return a shareflow with null value`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            //WHEN
            val result = sutOrderLocalCache.deleteProductToCurrentOrder(productMock)

            val sharedFlow = sutOrderLocalCache.getCurrentOrder()
            val testCollector = sharedFlow.testIn(this)
            testCollector.cancel()
            val orderResponse = testCollector.values().first()

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(orderResponse, null)
        }
    }

    @Test
    fun `GIVEN a product, quantity and an appetizer WHEN addOrUpdateAppetizerToCurrentOrder THEN add them to the current order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            val sharedFlow = sutOrderLocalCache.getCurrentOrder()
            val testCollector = sharedFlow.testIn(this)
            testCollector.cancel()
            val orderResponse = testCollector.values().first()

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(orderResponse?.getOrderDetails()?.size, 1)
            Assert.assertEquals(orderResponse?.getOrderDetails()?.first()?.appetizers?.size, 1)
            Assert.assertEquals(orderResponse?.getOrderDetails()?.first()?.quantity, 2)
        }
    }

    @Test
    fun `GIVEN a product, quantity and an appetizer WHEN addOrUpdateAppetizerToCurrentOrder and the product is not in the order THEN do not add or update the appetizer`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val unknownProduct: Product = mock()
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                unknownProduct
            )

            val sharedFlow = sutOrderLocalCache.getCurrentOrder()
            val testCollector = sharedFlow.testIn(this)
            testCollector.cancel()
            val orderResponse = testCollector.values().first()

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(orderResponse?.getOrderDetails()?.first()?.appetizers?.size, 0)
        }
    }

    @Test
    fun `GIVEN a product WHEN fetchAppetizersByProduct THEN return the list of appetizers related to that product`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)
            sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            //WHEN
            val result = sutOrderLocalCache.fetchAppetizersByProduct(productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, 1)
            Assert.assertEquals(result.first().quantity, 2)
        }
    }

    @Test
    fun `GIVEN a product WHEN fetchAppetizersByProduct and the product is not added to the order THEN return an empty list`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val unknownProduct: Product = mock()

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)
            sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            //WHEN
            val result = sutOrderLocalCache.fetchAppetizersByProduct(unknownProduct)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, 0)
        }
    }

    @Test
    fun `GIVEN a product WHEN clearAppetizersByProduct THEN clear the appetizers related to that product`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)
            sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            //WHEN
            val result = sutOrderLocalCache.clearAppetizersByProduct(productMock)

            val appetizersResult = sutOrderLocalCache.fetchAppetizersByProduct(productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(appetizersResult.size, 0)
        }
    }

    @Test
    fun `GIVEN a product WHEN clearAppetizersByProduct and there is an unknown product for the order THEN do not clear the appetizer of any other product`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val appetizerMock = Product(
                code = "001",
                name = "Appetizer 1",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )
            val quantityMock = 2

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            val mockUnknownProduct: Product = mock()
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)
            sutOrderLocalCache.addOrUpdateAppetizerToCurrentOrder(
                quantityMock,
                appetizerMock,
                productMock
            )

            //WHEN
            val result = sutOrderLocalCache.clearAppetizersByProduct(mockUnknownProduct)

            val appetizersResult = sutOrderLocalCache.fetchAppetizersByProduct(productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(appetizersResult.size, 1)
        }
    }

    @Test
    fun `GIVEN a product WHEN clearAppetizersByProduct and the product does not have appetizers THEN do not clear the appetizers`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val productMock = Product(
                code = "Marli",
                name = "Colin",
                description = "Darron",
                imageUrl = "See",
                measure = Measure(code = "Angelia", value = "Brandin"),
                currency = Currency(code = "Sally", value = "Takeshia"),
                stock = 2292,
                price = 85.592,
                productType = MenuDish.build(code = "Concepcion", name = "Giorgio"),
                saleStrategy = null
            )

            val mockVendor = Vendor(
                tableId = "Shireen",
                restaurantId = "Akash",
                additionalInfo = mapOf("restaurant_own_delivery" to true),
                paymentMethod = listOf()
            )
            whenever(mockVendorShared.fetch()).doReturn(mockVendor)
            createOrder(productMock)

            //WHEN
            val result = sutOrderLocalCache.clearAppetizersByProduct(productMock)

            val appetizersResult = sutOrderLocalCache.fetchAppetizersByProduct(productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(appetizersResult.size, 0)
        }
    }

    private suspend fun createOrder(productMock: Product) {
        sutOrderLocalCache.addOrUpdateProductToCurrentOrder(2, productMock)
    }
}