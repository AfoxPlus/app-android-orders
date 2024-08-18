package com.afoxplus.orders.usecases

import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.domain.usecases.VendorShareUseCase
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
class VendorShareUseCaseTests {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockVendor: VendorShared = mock()
    private val sutUseCase: VendorShareUseCase by lazy {
        VendorShareUseCase(
            mockVendor
        )
    }

    @Test
    fun `WHEN getRestaurantPayments THEN return the restaurant name`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockRestaurantName = "Ya Listo"
            val responseMock = Vendor(
                tableId = "Brett",
                restaurantId = "Cyrena",
                additionalInfo = mapOf("restaurant_name" to mockRestaurantName),
                paymentMethod = listOf()
            )
            whenever(mockVendor.fetch()).doReturn(responseMock)

            //WHEN
            val result = sutUseCase.getRestaurantName()

            //THEN
            verify(mockVendor).fetch()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockRestaurantName)
        }
    }

    @Test
    fun `WHEN getRestaurantPayments THEN return an empty string when when vendor fetch is null`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockRestaurantName = ""
            whenever(mockVendor.fetch()).doReturn(null)

            //WHEN
            val result = sutUseCase.getRestaurantName()

            //THEN
            verify(mockVendor).fetch()
            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockRestaurantName)
        }
    }
}