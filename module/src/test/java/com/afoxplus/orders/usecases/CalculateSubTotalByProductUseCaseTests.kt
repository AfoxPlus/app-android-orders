package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.usecases.CalculateSubTotalByProductUseCase
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.MenuDish
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CalculateSubTotalByProductUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val sutUseCase: CalculateSubTotalByProductUseCase by lazy {
        CalculateSubTotalByProductUseCase()
    }

    @Test
    fun `GIVEN a quantity and a product WHEN calculateSubTotalByProduct THEN return product price for sale times quantity`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val priceForSaleMock = 66.534
            val productMock = Product(
                code = "Eleazar",
                name = "Zane",
                description = "Daneille",
                imageUrl = "Brianna",
                measure = Measure(code = "Justine", value = "Hortencia"),
                currency = Currency(code = "Taya", value = "Mateo"),
                stock = 4294,
                price = priceForSaleMock,
                productType = MenuDish.build(code = "Deborah", name = "Jabari"),
                saleStrategy = null
            )
            val quantityMock = 2
            val expectedSubTotal = priceForSaleMock.times(quantityMock)

            //WHEN
            val result = sutUseCase.invoke(quantityMock, productMock)

            //THEN
            Assert.assertNotNull(result)
            Assert.assertEquals(result, expectedSubTotal, 0.0)
        }
    }
}