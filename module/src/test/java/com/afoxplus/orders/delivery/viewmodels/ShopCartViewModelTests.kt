package com.afoxplus.orders.delivery.viewmodels

import com.afoxplus.orders.cross.UIKitCoroutinesDispatcherTest
import com.afoxplus.orders.cross.exceptions.ApiErrorException
import com.afoxplus.orders.cross.exceptions.ExceptionMessage
import com.afoxplus.orders.cross.exceptions.OrderBusinessException
import com.afoxplus.orders.delivery.models.SendOrderStatusUIModel
import com.afoxplus.orders.domain.entities.Client
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.entities.OrderType
import com.afoxplus.orders.domain.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetRestaurantNameUseCase
import com.afoxplus.orders.domain.usecases.GetRestaurantPaymentsUseCase
import com.afoxplus.orders.domain.usecases.SendOrderUseCase
import com.afoxplus.orders.utils.BaseViewModelTest
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.MenuDish
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.common.ResultState
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ShopCartViewModelTests : BaseViewModelTest() {
    private val mockAddOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrderUseCase =
        mock()
    private val mockGetCurrentOrder: GetCurrentOrderUseCase = mock()
    private val mockDeleteProductToCurrentOrder: DeleteProductToCurrentOrderUseCase = mock()
    private val mockSendOrder: SendOrderUseCase = mock()
    private val mockGetRestaurantName: GetRestaurantNameUseCase = mock()
    private val mockGetRestaurantPaymentsUseCase: GetRestaurantPaymentsUseCase = mock()
    private val mockDispatcher: UIKitCoroutineDispatcher by lazy { UIKitCoroutinesDispatcherTest() }

    private val sutViewModel: ShopCartViewModel by lazy {
        ShopCartViewModel(
            mockAddOrUpdateProductToCurrentOrder,
            mockGetCurrentOrder,
            mockDeleteProductToCurrentOrder,
            mockSendOrder,
            mockGetRestaurantName,
            mockGetRestaurantPaymentsUseCase,
            mockDispatcher
        )
    }


    @Test
    fun `WHEN loadData THEN load current order`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)

            //WHEN
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //THEN
            verify(mockGetCurrentOrder).invoke()
            Assert.assertNotNull(sutViewModel.order.value)
            Assert.assertEquals(sutViewModel.order.value, mockOrder)
        }
    }

    @Test
    fun `WHEN loadData THEN load payment methods`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockPaymentMethods: List<PaymentMethod> = createPaymentMethods()

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockGetRestaurantPaymentsUseCase.invoke()).thenReturn(mockPaymentMethods)

            //WHEN
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //THEN
            verify(mockGetCurrentOrder).invoke()
            verify(mockGetRestaurantPaymentsUseCase).invoke()

            Assert.assertNotNull(sutViewModel.order.value)
            Assert.assertNotNull(sutViewModel.paymentMethodSelectedLiveData.value)
            Assert.assertEquals(
                sutViewModel.paymentMethodSelectedLiveData.value,
                mockPaymentMethods.first()
            )
        }
    }

    @Test
    fun `WHEN loadData and there is no order THEN close the screen`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)

            //WHEN
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(null)

            //THEN
            verify(mockGetCurrentOrder).invoke()
            Assert.assertNull(sutViewModel.order.value)
            Assert.assertNotNull(sutViewModel.eventOnBackSendOrder.value)
            Assert.assertTrue(sutViewModel.eventOnBackSendOrder.value is UIKitEvent)
        }
    }

    @Test
    fun `WHEN loadData and there is an order with no details THEN close the screen`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockEmptyOrder: Order = createEmptyOrder()

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)

            //WHEN
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockEmptyOrder)

            //THEN
            verify(mockGetCurrentOrder).invoke()
            Assert.assertNull(sutViewModel.order.value)
            Assert.assertNotNull(sutViewModel.eventOnBackSendOrder.value)
            Assert.assertTrue(sutViewModel.eventOnBackSendOrder.value is UIKitEvent)
        }
    }

    @Test
    fun `WHEN deleteItem THEN call deleteProductToCurrentOrder`() {
        runTest {
            //GIVEN
            val mockOrderDetail: OrderDetail = mock()

            //WHEN
            sutViewModel.deleteItem(mockOrderDetail)

            //THEN
            verify(mockDeleteProductToCurrentOrder).invoke(mockOrderDetail.product)
        }
    }

    @Test
    fun `WHEN updateQuantity THEN call addOrUpdateProductToCurrentOrder`() {
        runTest {
            //GIVEN
            val mockOrderDetail: OrderDetail = mock()
            val mockQuantity = 2

            //WHEN
            sutViewModel.updateQuantity(mockOrderDetail, mockQuantity)

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder).invoke(
                mockQuantity,
                mockOrderDetail.product
            )
        }
    }

    @Test
    fun `WHEN editMenuDish THEN send event to open the add card screen`() {
        runTest {
            //GIVEN
            val mockOrderDetail: OrderDetail = mock()

            //WHEN
            sutViewModel.editMenuDish(mockOrderDetail)

            //THEN
            Assert.assertNotNull(sutViewModel.goToAddCardProductEvent.value)
            Assert.assertTrue(sutViewModel.goToAddCardProductEvent.value is UIKitEvent)
        }
    }

    @Test
    fun `GIVEN isOrderCartView as true WHEN handleClickSender THEN open table page`() {
        runTest {
            //GIVEN
            val mockIsOrderCartView = true
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)

            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.handleClickSender(mockIsOrderCartView)

            //THEN
            Assert.assertNotNull(sutViewModel.eventOpenTableOrder.value)
            Assert.assertTrue(sutViewModel.eventOpenTableOrder.value is UIKitEvent)
            Assert.assertEquals(
                sutViewModel.nameButtonSendOrderLiveData.value,
                mockOrder.getLabelSendMyOrder()
            )
        }
    }

    @Test
    fun `GIVEN isOrderCartView as false WHEN handleClickSender THEN send event to open the add card screen`() {
        runTest {
            //GIVEN
            val mockIsOrderCartView = false
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)

            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.handleClickSender(mockIsOrderCartView)

            //THEN
            Assert.assertNotNull(sutViewModel.eventValidateTableOrder.value)
            Assert.assertTrue(sutViewModel.eventValidateTableOrder.value is UIKitEvent)
        }
    }

    @Test
    fun `GIVEN isTableOrder as true WHEN handleBackPressed THEN remove order table and change the name of the button to Continuar`() {
        runTest {
            //GIVEN
            val mockIsTableOrder = true

            //WHEN
            sutViewModel.handleBackPressed(mockIsTableOrder)

            //THEN
            Assert.assertNotNull(sutViewModel.eventRemoveTableOrder.value)
            Assert.assertTrue(sutViewModel.eventRemoveTableOrder.value is UIKitEvent)
            Assert.assertEquals(sutViewModel.nameButtonSendOrderLiveData.value, "Continuar")
        }
    }

    @Test
    fun `GIVEN isTableOrder as false WHEN handleBackPressed THEN send event to close the screen`() {
        runTest {
            //GIVEN
            val mockIsTableOrder = false

            //WHEN
            sutViewModel.handleBackPressed(mockIsTableOrder)

            //THEN
            Assert.assertNotNull(sutViewModel.eventOnBackSendOrder.value)
            Assert.assertTrue(sutViewModel.eventOnBackSendOrder.value is UIKitEvent)
        }
    }


    @Test
    fun `WHEN retrySendOrder THEN send the order and handle the success result`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockOrderResult: ResultState.Success<String> =
                ResultState.Success("This is a success")

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockSendOrder.invoke(mockOrder)).doReturn(mockOrderResult)

            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.retrySendOrder(delayTime = 0L)

            //THEN
            verify(mockSendOrder).invoke(mockOrder)
            Assert.assertNotNull(sutViewModel.eventOpenSuccessOrder.value)
            Assert.assertTrue(sutViewModel.eventOpenSuccessOrder.value is SendOrderStatusUIModel.Success)
            Assert.assertEquals(
                (sutViewModel.eventOpenSuccessOrder.value as SendOrderStatusUIModel.Success).message,
                mockOrderResult.data
            )
        }
    }

    @Test
    fun `WHEN retrySendOrder and the it has been retrying more than 4 times THEN send an error to the screen`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockOrderResult: ResultState.Success<String> =
                ResultState.Success("This is a success")

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockSendOrder.invoke(mockOrder)).doReturn(mockOrderResult)

            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.retrySendOrder(delayTime = 0L)
            sutViewModel.retrySendOrder(delayTime = 0L)
            sutViewModel.retrySendOrder(delayTime = 0L)
            sutViewModel.retrySendOrder(delayTime = 0L)
            sutViewModel.retrySendOrder(delayTime = 0L)

            //THEN
            verify(mockSendOrder, times(4)).invoke(mockOrder)
            Assert.assertNotNull(sutViewModel.eventOpenSuccessOrder.value)
            Assert.assertTrue(sutViewModel.eventOpenSuccessOrder.value is SendOrderStatusUIModel.Error)
            Assert.assertTrue(
                (sutViewModel.eventOpenSuccessOrder.value as SendOrderStatusUIModel.Error).exception is OrderBusinessException
            )
        }
    }

    @Test
    fun `WHEN retrySendOrder and there is an error with sending the order THEN send an error to the screen`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order?> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order?> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockOrderResult: ResultState.Error<String> = ResultState.Error(
                ApiErrorException(
                    contentMessage = ExceptionMessage(
                        "This is an error",
                        info = ""
                    )
                )
            )

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockSendOrder.invoke(mockOrder)).doReturn(mockOrderResult)

            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.retrySendOrder(delayTime = 0L)

            //THEN
            verify(mockSendOrder).invoke(mockOrder)
            Assert.assertNotNull(sutViewModel.eventOpenSuccessOrder.value)
            Assert.assertTrue(sutViewModel.eventOpenSuccessOrder.value is SendOrderStatusUIModel.Error)
            Assert.assertTrue(
                (sutViewModel.eventOpenSuccessOrder.value as SendOrderStatusUIModel.Error).exception is ApiErrorException
            )
        }
    }

    @Test
    fun `WHEN restaurantName THEN load restaurant name`() {
        runTest {
            //GIVEN

            val mockRestaurantName = "Ya Listo"

            whenever(mockGetRestaurantName.invoke()).thenReturn(mockRestaurantName)

            //WHEN
            val result = sutViewModel.restaurantName()

            //THEN
            verify(mockGetRestaurantName).invoke()
            Assert.assertEquals(result, mockRestaurantName)
        }
    }

    @Test
    fun `WHEN fetchPaymentMethods THEN load payment methods`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockPaymentMethods: List<PaymentMethod> = createPaymentMethods()

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockGetRestaurantPaymentsUseCase.invoke()).thenReturn(mockPaymentMethods)
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            val result = sutViewModel.fetchPaymentMethods()

            //THEN
            verify(mockGetCurrentOrder).invoke()
            verify(mockGetRestaurantPaymentsUseCase).invoke()

            Assert.assertNotNull(result)
            Assert.assertEquals(result, mockPaymentMethods)
        }
    }

    @Test
    fun `GIVEN the payment method WHEN selectPaymentMethod THEN change the state for the other payment methods as false`() {
        runTest {
            //GIVEN
            val mutableOrderSharedFlow: MutableSharedFlow<Order> = MutableSharedFlow()
            val mockOrderFlow: SharedFlow<Order> = mutableOrderSharedFlow
            val mockOrder: Order = createOrder()
            val mockPaymentMethods: List<PaymentMethod> = createPaymentMethods()
            val mockSelectedPaymentMethod = PaymentMethod(id = "yape", name = "Rock", isSelected = false)

            whenever(mockGetCurrentOrder.invoke()).thenReturn(mockOrderFlow)
            whenever(mockGetRestaurantPaymentsUseCase.invoke()).thenReturn(mockPaymentMethods)
            sutViewModel.loadData()
            mutableOrderSharedFlow.emit(mockOrder)

            //WHEN
            sutViewModel.selectPaymentMethod(mockSelectedPaymentMethod)
            val paymentMethods = sutViewModel.fetchPaymentMethods()

            //THEN
            Assert.assertEquals(sutViewModel.paymentMethodSelectedLiveData.value, mockSelectedPaymentMethod)
            paymentMethods.forEach {
                if (it.id == mockSelectedPaymentMethod.id)
                    Assert.assertTrue(it.isSelected)
                else
                    Assert.assertTrue(!it.isSelected)
            }
        }
    }

    private fun createPaymentMethods(): List<PaymentMethod> {
        return arrayListOf(
            PaymentMethod(id = "plin", name = "Rock", isSelected = false),
            PaymentMethod(id = "yape", name = "Roxanne", isSelected = false),
            PaymentMethod(id = "Judd", name = "Roxanne", isSelected = false),
            PaymentMethod(id = "Juddss", name = "Roxanne", isSelected = false)
        )
    }

    private fun createClient(): Client {
        return Client(name = "Wayne", phone = "9212322", addressReference = "299928882")
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

    private fun createOrderForLocal(): Order {
        return Order(
            code = "Chancy",
            orderType = OrderType.Local,
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
}