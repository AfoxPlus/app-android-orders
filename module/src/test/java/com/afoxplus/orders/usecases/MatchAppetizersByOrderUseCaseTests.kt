package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.usecases.FetchAppetizerByOrderUseCase
import com.afoxplus.orders.domain.usecases.MatchAppetizersByOrderUseCase
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.MenuDish
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MatchAppetizersByOrderUseCaseTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockFetchAppetizerByOrderUseCase: FetchAppetizerByOrderUseCase = mock()
    private val sutUseCase: MatchAppetizersByOrderUseCase by lazy {
        MatchAppetizersByOrderUseCase(
            mockFetchAppetizerByOrderUseCase
        )
    }

    @Test
    fun `GIVEN appetizers and a product WHEN matchAppetizersByOrder THEN return a list of appetizer matching with the ones added to the order`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockAppetizers: List<Product> = createAppetizers()
            val mockProduct: Product = mock()
            val mockOrderAppetizers: List<OrderAppetizerDetail> = createOrderAppetizers()
            val mockResponse: List<OrderAppetizerDetail> = createOrderAppetizersDetails()
            whenever(mockFetchAppetizerByOrderUseCase.invoke(mockProduct)).doReturn(
                mockOrderAppetizers
            )

            //WHEN
            val result = sutUseCase.invoke(mockAppetizers, mockProduct)

            //THEN
            verify(mockFetchAppetizerByOrderUseCase).invoke(mockProduct)
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, mockResponse.size)
            Assert.assertEquals(result[0].quantity, mockResponse[0].quantity)
            Assert.assertEquals(result[0].product.code, mockResponse[0].product.code)
        }
    }


    private fun createOrderAppetizersDetails(): List<OrderAppetizerDetail> {
        return arrayListOf(
            OrderAppetizerDetail(
                product = Product(
                    code = "Ashly",
                    name = "Papa a la huancaina",
                    description = "Christy",
                    imageUrl = "Jamell",
                    measure = Measure(code = "Keeley", value = "Lauryn"),
                    currency = Currency(code = "Britnie", value = "Sina"),
                    stock = 5881,
                    price = 6.612,
                    productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                    saleStrategy = null
                ),
                quantity = 2
            ),
            OrderAppetizerDetail(
                product = Product(
                    code = "Ashly2",
                    name = "Sopa de pollo",
                    description = "Christy",
                    imageUrl = "Jamell",
                    measure = Measure(code = "Keeley", value = "Lauryn"),
                    currency = Currency(code = "Britnie", value = "Sina"),
                    stock = 5881,
                    price = 6.612,
                    productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                    saleStrategy = null
                ),
                quantity = 0
            ),
            OrderAppetizerDetail(
                product = Product(
                    code = "Ashly3",
                    name = "Causa",
                    description = "Christy",
                    imageUrl = "Jamell",
                    measure = Measure(code = "Keeley", value = "Lauryn"),
                    currency = Currency(code = "Britnie", value = "Sina"),
                    stock = 5881,
                    price = 6.612,
                    productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                    saleStrategy = null
                ),
                quantity = 0
            )
        )
    }

    private fun createOrderAppetizers(): List<OrderAppetizerDetail> {
        return arrayListOf(
            OrderAppetizerDetail(
                product =
                Product(
                    code = "Ashly",
                    name = "Papa a la huancaina",
                    description = "Christy",
                    imageUrl = "Jamell",
                    measure = Measure(code = "Keeley", value = "Lauryn"),
                    currency = Currency(code = "Britnie", value = "Sina"),
                    stock = 5881,
                    price = 6.612,
                    productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                    saleStrategy = null
                ),
                quantity = 2
            )
        )
    }

    private fun createAppetizers(): List<Product> {
        return arrayListOf(
            Product(
                code = "Ashly",
                name = "Papa a la huancaina",
                description = "Christy",
                imageUrl = "Jamell",
                measure = Measure(code = "Keeley", value = "Lauryn"),
                currency = Currency(code = "Britnie", value = "Sina"),
                stock = 5881,
                price = 6.612,
                productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                saleStrategy = null
            ),
            Product(
                code = "Ashly2",
                name = "Sopa de pollo",
                description = "Christy",
                imageUrl = "Jamell",
                measure = Measure(code = "Keeley", value = "Lauryn"),
                currency = Currency(code = "Britnie", value = "Sina"),
                stock = 5881,
                price = 6.612,
                productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                saleStrategy = null
            ),
            Product(
                code = "Ashly3",
                name = "Causa",
                description = "Christy",
                imageUrl = "Jamell",
                measure = Measure(code = "Keeley", value = "Lauryn"),
                currency = Currency(code = "Britnie", value = "Sina"),
                stock = 5881,
                price = 6.612,
                productType = MenuDish.build(code = "Dyanna", name = "Alica"),
                saleStrategy = null
            )
        )
    }
}