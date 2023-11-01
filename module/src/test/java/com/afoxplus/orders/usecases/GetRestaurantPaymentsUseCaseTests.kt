package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.usecases.GetRestaurantPaymentsUseCase
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.afoxplus.uikit.objects.vendor.Vendor
import com.afoxplus.uikit.objects.vendor.VendorShared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetRestaurantPaymentsUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockVendor: VendorShared = mock()
    private val sutUseCase: GetRestaurantPaymentsUseCase by lazy {
        GetRestaurantPaymentsUseCase(
            mockVendor
        )
    }

    @Test
    fun `WHEN getRestaurantPayments THEN return a list of payment methods`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val responseMock = Vendor(
                tableId = "Brett",
                restaurantId = "Cyrena",
                additionalInfo = mapOf(),
                paymentMethod = listOf(
                    PaymentMethod(
                        id = "Serge",
                        name = "Mira",
                        isSelected = true
                    ),
                    PaymentMethod(
                        id = "Cristina",
                        name = "Janeen",
                        isSelected = true
                    )
                )
            )
            whenever(mockVendor.fetch()).doReturn(responseMock)


            //WHEN
            val result = sutUseCase.invoke()

            //THEN
            verify(mockVendor).fetch()
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, responseMock.paymentMethod.size)
            Assert.assertEquals(result[0].id, responseMock.paymentMethod[0].id)
        }
    }

    @Test
    fun `WHEN getRestaurantPayments THEN return an empty list when the vendor fetch is null`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            whenever(mockVendor.fetch()).doReturn(null)


            //WHEN
            val result = sutUseCase.invoke()

            //THEN
            verify(mockVendor).fetch()
            Assert.assertNotNull(result)
            Assert.assertEquals(result.size, 0)
        }
    }
}