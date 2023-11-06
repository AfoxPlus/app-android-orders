package com.afoxplus.orders.delivery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.UIKitCoroutinesDispatcherTest
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.entities.OrderType
import com.afoxplus.orders.domain.usecases.ClearCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetRestaurantNameUseCase
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.MenuDish
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MarketOrderViewModelTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockEventBusListener: UIKitEventBusWrapper = mock()
    private val mockClearCurrentOrder: ClearCurrentOrderUseCase = mock()
    private val mockGetCurrentOrder: GetCurrentOrderUseCase = mock()
    private val mockGetRestaurantName: GetRestaurantNameUseCase = mock()
    private val mockDispatcher: UIKitCoroutineDispatcher by lazy { UIKitCoroutinesDispatcherTest() }

    private val sutViewModel: MarketOrderViewModel by lazy {
        MarketOrderViewModel(
            mockEventBusListener,
            mockClearCurrentOrder,
            mockGetCurrentOrder,
            mockGetRestaurantName,
            mockDispatcher
        )
    }

    @Test
    fun `WHEN loadCurrentOrder THEN fill the order on mOrder liveData`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order> = MutableSharedFlow()
            val mockOrder: SharedFlow<Order> = mutableOrderSharedFlow
            whenever(mockGetCurrentOrder.invoke()).doReturn(mockOrder)

            //WHEN
            sutViewModel.loadCurrentOrder()
            mutableOrderSharedFlow.emit(createOrder())

            //THEN
            verify(mockGetCurrentOrder).invoke()
            Assert.assertNotNull(sutViewModel.order.value)
            Assert.assertTrue(sutViewModel.order.value?.code == "Chancy")
        }
    }

    @Test
    fun `WHEN loadCurrentOrder and the order is null THEN mOrder liveData should be null as well`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrder: SharedFlow<Order?> = mutableOrderSharedFlow
            whenever(mockGetCurrentOrder.invoke()).doReturn(mockOrder)

            //WHEN
            sutViewModel.loadCurrentOrder()
            mutableOrderSharedFlow.emit(null)

            //THEN
            verify(mockGetCurrentOrder).invoke()
            Assert.assertEquals(sutViewModel.order.value, null)
        }
    }

    @Test
    fun `WHEN restaurantName THEN return the restaurant name`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockRestaurantName = "Ya listo"
            whenever(mockGetRestaurantName.invoke()).doReturn(mockRestaurantName)

            //WHEN
            val result = sutViewModel.restaurantName()

            //THEN
            verify(mockGetRestaurantName).invoke()
            Assert.assertNotNull(result)
            Assert.assertEquals(mockRestaurantName, result)

        }
    }

    @Test
    fun `WHEN onClickViewOrder THEN emit the value to onMarketOrderEvent`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrder: SharedFlow<Order?> = mutableOrderSharedFlow
            whenever(mockGetCurrentOrder.invoke()).doReturn(mockOrder)
            sutViewModel.loadCurrentOrder()
            mutableOrderSharedFlow.emit(createOrder())

            //WHEN
            sutViewModel.onClickViewOrder()

            //THEN
            sutViewModel.viewModelScope.launch {
                sutViewModel.onMarketOrderEvent.collect {
                    Assert.assertTrue(it is MarketOrderViewModel.MarketOrderEvent.OnClickViewOrder)
                    Assert.assertEquals(
                        (it as MarketOrderViewModel.MarketOrderEvent.OnClickViewOrder).order.code,
                        "Chancy"
                    )
                }
            }
        }
    }

    @Test
    fun `WHEN clearOrderAndGoBack THEN clear the order and send the back event`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            //WHEN
            sutViewModel.clearOrderAndGoBack()

            //THEN
            verify(mockClearCurrentOrder).invoke()
            sutViewModel.viewModelScope.launch {
                sutViewModel.onMarketOrderEvent.collect {
                    Assert.assertTrue(it is MarketOrderViewModel.MarketOrderEvent.OnBackPressed)
                }
            }
        }
    }

    @Test
    fun `WHEN onBackPressed and the order is null THEN clear the order and send the back event`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrder: SharedFlow<Order?> = mutableOrderSharedFlow
            whenever(mockGetCurrentOrder.invoke()).doReturn(mockOrder)
            sutViewModel.loadCurrentOrder()
            mutableOrderSharedFlow.emit(createEmptyOrder())

            //WHEN
            sutViewModel.onBackPressed()

            //THEN
            verify(mockClearCurrentOrder).invoke()
            sutViewModel.viewModelScope.launch {
                sutViewModel.onMarketOrderEvent.collect {
                    Assert.assertTrue(it is MarketOrderViewModel.MarketOrderEvent.OnBackPressed)
                }
            }
        }
    }

    @Test
    fun `WHEN onBackPressed and the order is not empty THEN display the modal`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrder: SharedFlow<Order?> = mutableOrderSharedFlow
            whenever(mockGetCurrentOrder.invoke()).doReturn(mockOrder)
            sutViewModel.loadCurrentOrder()
            mutableOrderSharedFlow.emit(createOrder())
            //WHEN
            sutViewModel.onBackPressed()

            //THEN
            sutViewModel.viewModelScope.launch {
                Assert.assertNotNull(sutViewModel.displayOrderModalLiveData.value)
            }
        }
    }

    private fun createOrder(): Order {
        return Order(
            code = "Chancy",
            orderType = OrderType.Delivery,
            restaurantId = "Bethanne",
            client = null,
            paymentMethod = null,
            orderDetails = arrayListOf(
                OrderDetail(
                    product = Product(
                        code = "Matilde",
                        name = "Venisha",
                        description = "Meliss",
                        imageUrl = "Denetra",
                        measure = Measure(code = "Falen", value = "Tina"),
                        currency = Currency(code = "Karrie", value = "Brenton"),
                        stock = 5302,
                        price = 27.752,
                        productType = MenuDish.build(
                            code = "Jacquline", name = "Larita"
                        ),
                        saleStrategy = null
                    ),
                    quantity = 1697,
                    saleOrderItemStrategy = null,
                    appetizers = arrayListOf()
                )
            ),
            saleOrderStrategy = null
        )
    }

    private fun createEmptyOrder(): Order {
        return Order(
            code = "Chancy",
            orderType = OrderType.Delivery,
            restaurantId = "Bethanne",
            client = null,
            paymentMethod = null,
            orderDetails = arrayListOf(),
            saleOrderStrategy = null
        )
    }
}