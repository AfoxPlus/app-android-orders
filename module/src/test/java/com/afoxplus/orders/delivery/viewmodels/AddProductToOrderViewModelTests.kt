package com.afoxplus.orders.delivery.viewmodels

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.R
import com.afoxplus.orders.cross.TestCoroutineRule
import com.afoxplus.orders.cross.UIKitCoroutinesDispatcherTest
import com.afoxplus.orders.delivery.views.events.AddedProductToCurrentOrderSuccessfullyEvent
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.usecases.AddOrUpdateAppetizerToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.CalculateSubTotalByProductUseCase
import com.afoxplus.orders.domain.usecases.ClearAppetizersOrderUseCase
import com.afoxplus.orders.domain.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.FindProductInOrderUseCase
import com.afoxplus.orders.domain.usecases.MatchAppetizersByOrderUseCase
import com.afoxplus.products.entities.Currency
import com.afoxplus.products.entities.Measure
import com.afoxplus.products.entities.Product
import com.afoxplus.products.entities.types.CartaDish
import com.afoxplus.products.entities.types.MenuDish
import com.afoxplus.products.usecases.actions.FetchAppetizerByCurrentRestaurant
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AddProductToOrderViewModelTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockEventBusListener: UIKitEventBusWrapper = mock()
    private val mockFindProductInOrder: FindProductInOrderUseCase = mock()
    private val mockCalculateSubTotalByProduct: CalculateSubTotalByProductUseCase = mock()
    private val mockAddOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrderUseCase =
        mock()
    private val mockDeleteProductToCurrentOrder: DeleteProductToCurrentOrderUseCase = mock()
    private val mockFetchAppetizerByCurrentRestaurant: FetchAppetizerByCurrentRestaurant = mock()
    private val mockMatchAppetizersByOrder: MatchAppetizersByOrderUseCase = mock()
    private val mockAddOrUpdateAppetizerToCurrentOrder: AddOrUpdateAppetizerToCurrentOrderUseCase =
        mock()
    private val mockClearAppetizersOrder: ClearAppetizersOrderUseCase = mock()
    private val mockDispatcher: UIKitCoroutineDispatcher by lazy { UIKitCoroutinesDispatcherTest() }

    private val sutViewModel: AddProductToOrderViewModel by lazy {
        AddProductToOrderViewModel(
            mockEventBusListener,
            mockFindProductInOrder,
            mockCalculateSubTotalByProduct,
            mockAddOrUpdateProductToCurrentOrder,
            mockDeleteProductToCurrentOrder,
            mockFetchAppetizerByCurrentRestaurant,
            mockMatchAppetizersByOrder,
            mockAddOrUpdateAppetizerToCurrentOrder,
            mockClearAppetizersOrder,
            mockDispatcher
        )
    }

    @Test
    fun `GIVEN a product WHEN startWithProduct and findProductInOrder return a menu order THEN fill productLiveData, find the product in the order and appetizerVisibility is Visible, fetchAppetizers, and matchAppetizers`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            //WHEN
            sutViewModel.startWithProduct(mockProduct)

            //THEN
            verify(mockFindProductInOrder).invoke(mockProduct)
            verify(mockFetchAppetizerByCurrentRestaurant).invoke()
            verify(mockMatchAppetizersByOrder).invoke(mockAppetizers, mockProduct)
            Assert.assertNotNull(sutViewModel.product.value)
            Assert.assertEquals(sutViewModel.quantity.value, mockOrderDetail.quantity)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertEquals(
                sutViewModel.buttonSubTotalState.value?.title,
                R.string.orders_market_update_product
            )
            Assert.assertEquals(sutViewModel.appetizerVisibility.value, View.VISIBLE)
            Assert.assertNotNull(sutViewModel.appetizersStateModel.value)
            Assert.assertEquals(sutViewModel.appetizersStateModel.value?.size, mockAppetizers.size)
        }
    }

    @Test
    fun `GIVEN a product WHEN startWithProduct and findProductInOrder return an order THEN fill productLiveData, find the product in the order and appetizerVisibility is GONE`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockCartaProduct()
            val mockOrderDetail: OrderDetail = createOrderDetail()
            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)

            //WHEN
            sutViewModel.startWithProduct(mockProduct)

            //THEN
            verify(mockFindProductInOrder).invoke(mockProduct)
            Assert.assertNotNull(sutViewModel.product.value)
            Assert.assertEquals(sutViewModel.quantity.value, mockOrderDetail.quantity)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertEquals(sutViewModel.appetizerVisibility.value, View.GONE)

        }
    }


    @Test
    fun `GIVEN a product WHEN startWithProduct and findProductInOrder return null order detail THEN put quantity liveData as null`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mock()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(null)

            //WHEN
            sutViewModel.startWithProduct(mockProduct)

            //THEN
            verify(mockFindProductInOrder).invoke(mockProduct)
            Assert.assertNotNull(sutViewModel.product.value)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertEquals(
                sutViewModel.buttonSubTotalState.value?.title,
                R.string.orders_market_add_product
            )
            Assert.assertEquals(sutViewModel.quantity.value, null)
        }
    }

    @Test
    fun `GIVEN the quantity WHEN calculateSubTotalByProduct and product is a cart THEN call addOrUpdateProductToCurrentOrder and calculateSubTotalByProduct`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockCartaProduct()
            val mockQuantity = 2
            val mockSubTotal = mockProduct.getPriceForSale().times(mockQuantity)

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)

            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.calculateSubTotalByProduct(mockQuantity)

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder).invoke(mockQuantity, mockProduct)
            verify(mockCalculateSubTotalByProduct).invoke(mockQuantity, mockProduct)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
        }
    }


    @Test
    fun `GIVEN the quantity WHEN calculateSubTotalByProduct and product is a menu THEN call addOrUpdateProductToCurrentOrder and calculateSubTotalByProduct`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockQuantity = 2
            val mockSubTotal = mockProduct.getPriceForSale().times(mockQuantity)
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)

            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.calculateSubTotalByProduct(mockQuantity)

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder).invoke(mockQuantity, mockProduct)
            verify(mockCalculateSubTotalByProduct).invoke(mockQuantity, mockProduct)
            verify(mockMatchAppetizersByOrder, times(2)).invoke(mockAppetizers, mockProduct)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertNotNull(sutViewModel.appetizersStateModel.value)
            Assert.assertEquals(sutViewModel.appetizersStateModel.value?.size, mockAppetizers.size)
        }
    }

    @Test
    fun `GIVEN quantity WHEN calculateSubTotalByProduct, quantity is less that the previous one THEN call addOrUpdateProductToCurrentOrder, calculateSubTotalByProduct, clearAppetizersOrder and matchAppetizersByOrder`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockPreviousQuantity = 3
            val mockCurrentQuantity = 2
            val mockSubTotal = mockProduct.getPriceForSale().times(mockPreviousQuantity)
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockPreviousQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)

            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.calculateSubTotalByProduct(mockPreviousQuantity)
            sutViewModel.calculateSubTotalByProduct(mockCurrentQuantity)

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder).invoke(mockPreviousQuantity, mockProduct)
            verify(mockCalculateSubTotalByProduct).invoke(mockPreviousQuantity, mockProduct)
            verify(mockMatchAppetizersByOrder, times(3)).invoke(mockAppetizers, mockProduct)
            verify(mockClearAppetizersOrder).invoke(mockProduct)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertEquals(
                sutViewModel.buttonSubTotalState.value?.title,
                R.string.orders_market_update_product
            )
            Assert.assertNotNull(sutViewModel.appetizersStateModel.value)
            Assert.assertEquals(sutViewModel.appetizersStateModel.value?.size, mockAppetizers.size)
            Assert.assertEquals(sutViewModel.appetizersShowModal.value, Unit)
        }
    }

    @Test
    fun `GIVEN quantity WHEN calculateSubTotalByProduct, quantity is 0 THEN call addOrUpdateProductToCurrentOrder, calculateSubTotalByProduct, clearAppetizersOrder and matchAppetizersByOrder`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockPreviousQuantity = 1
            val mockCurrentQuantity = 0
            val mockSubTotal = mockProduct.getPriceForSale().times(mockPreviousQuantity)
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockPreviousQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)

            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.calculateSubTotalByProduct(mockPreviousQuantity)
            sutViewModel.calculateSubTotalByProduct(mockCurrentQuantity)

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder).invoke(mockPreviousQuantity, mockProduct)
            verify(mockCalculateSubTotalByProduct).invoke(mockPreviousQuantity, mockProduct)
            verify(mockMatchAppetizersByOrder, times(3)).invoke(mockAppetizers, mockProduct)
            verify(mockClearAppetizersOrder, times(2)).invoke(mockProduct)
            Assert.assertNotNull(sutViewModel.buttonSubTotalState.value)
            Assert.assertEquals(
                sutViewModel.buttonSubTotalState.value?.title,
                R.string.orders_market_delete_product
            )
            Assert.assertNotNull(sutViewModel.appetizersStateModel.value)
            Assert.assertEquals(sutViewModel.appetizersStateModel.value?.size, mockAppetizers.size)
            Assert.assertEquals(sutViewModel.appetizersShowModal.value, Unit)
        }
    }

    @Test
    fun `WHEN addOrUpdateToCurrentOrder, quantity is less that the previous one THEN call AddOrUpdateProductToCurrentOrder and EventBusListener`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockPreviousQuantity = 3
            val mockSubTotal = mockProduct.getPriceForSale().times(mockPreviousQuantity)
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockPreviousQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)
            sutViewModel.startWithProduct(mockProduct)
            sutViewModel.calculateSubTotalByProduct(mockPreviousQuantity)

            //WHEN
            sutViewModel.addOrUpdateToCurrentOrder()

            //THEN
            verify(mockAddOrUpdateProductToCurrentOrder, times(2)).invoke(
                mockPreviousQuantity,
                mockProduct
            )
            verify(mockEventBusListener).send(AddedProductToCurrentOrderSuccessfullyEvent)
            sutViewModel.viewModelScope.launch {
                sutViewModel.events.collect {
                    Assert.assertTrue(it is AddProductToOrderViewModel.Events.CloseScreen)
                }
            }
        }
    }


    @Test
    fun `GIVEN an appetizer and the quantity WHEN handleAppetizerQuantity THEN call AddOrUpdateAppetizerToCurrentOrder and MatchAppetizersByOrder`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockQuantity = 3
            val mockSubTotal = mockProduct.getPriceForSale().times(mockQuantity)
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockAppetizer: Product = mockAppetizers.first()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)
            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.handleAppetizerQuantity(mockAppetizer, mockQuantity)

            //THEN
            verify(mockAddOrUpdateAppetizerToCurrentOrder).invoke(
                mockQuantity,
                mockAppetizer,
                mockProduct
            )
            verify(mockMatchAppetizersByOrder, times(2)).invoke(mockAppetizers, mockProduct)
            Assert.assertNotNull(sutViewModel.appetizersStateModel.value)
            Assert.assertEquals(sutViewModel.appetizersStateModel.value?.size, mockAppetizers.size)
        }
    }


    @Test
    fun `WHEN onBackAction is a new order THEN call deleteProductToCurrentOrder and send the event CloseScreen`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockPreviousQuantity = 3
            val mockSubTotal = mockProduct.getPriceForSale().times(mockPreviousQuantity)
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(null)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockPreviousQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)
            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.onBackAction()

            //THEN
            verify(mockDeleteProductToCurrentOrder).invoke(mockProduct)
            sutViewModel.viewModelScope.launch {
                sutViewModel.events.collect {
                    Assert.assertTrue(it is AddProductToOrderViewModel.Events.CloseScreen)
                }
            }
        }
    }

    @Test
    fun `WHEN onBackAction is an update order THEN send the event CloseScreen`() {
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val mockProduct: Product = mockMenuProduct()
            val mockPreviousQuantity = 3
            val mockOrderDetail: OrderDetail = createOrderMenuDetail()
            val mockSubTotal = mockProduct.getPriceForSale().times(mockPreviousQuantity)
            val mockAppetizers: List<Product> = mockAppetizers()
            val mockOrderAppetizerDetail: List<OrderAppetizerDetail> = mockOrderAppetizers()

            whenever(mockFindProductInOrder.invoke(mockProduct)).doReturn(mockOrderDetail)
            whenever(mockFetchAppetizerByCurrentRestaurant.invoke()).doReturn(mockAppetizers)
            whenever(mockMatchAppetizersByOrder.invoke(mockAppetizers, mockProduct)).doReturn(
                mockOrderAppetizerDetail
            )

            whenever(
                mockCalculateSubTotalByProduct.invoke(
                    mockPreviousQuantity,
                    mockProduct
                )
            ).doReturn(mockSubTotal)
            sutViewModel.startWithProduct(mockProduct)

            //WHEN
            sutViewModel.onBackAction()

            //THEN
            verify(mockDeleteProductToCurrentOrder, times(0)).invoke(mockProduct)
            sutViewModel.viewModelScope.launch {
                sutViewModel.events.collect {
                    Assert.assertTrue(it is AddProductToOrderViewModel.Events.CloseScreen)
                }
            }
        }
    }

    private fun mockOrderAppetizers(): List<OrderAppetizerDetail> {
        return arrayListOf(
            OrderAppetizerDetail(
                Product(
                    code = "Kianna",
                    name = "Jerrold",
                    description = "Richmond",
                    imageUrl = "Migdalia",
                    measure = Measure(code = "Latishia", value = "Jermichael"),
                    currency = Currency(code = "Katharina", value = "Trena"),
                    stock = 4163,
                    price = 41.028,
                    productType = MenuDish.build(code = "Eamon", name = "Criselda"),
                    saleStrategy = null
                ), quantity = 1
            ),
            OrderAppetizerDetail(
                Product(
                    code = "Kianna2",
                    name = "Jerrold2",
                    description = "Richmond",
                    imageUrl = "Migdalia",
                    measure = Measure(code = "Latishia", value = "Jermichael"),
                    currency = Currency(code = "Katharina", value = "Trena"),
                    stock = 4163,
                    price = 41.028,
                    productType = MenuDish.build(code = "Eamon", name = "Criselda"),
                    saleStrategy = null
                ), quantity = 1
            )
        )
    }

    private fun mockAppetizers(): List<Product> {
        return arrayListOf(
            Product(
                code = "Kianna",
                name = "Jerrold",
                description = "Richmond",
                imageUrl = "Migdalia",
                measure = Measure(code = "Latishia", value = "Jermichael"),
                currency = Currency(code = "Katharina", value = "Trena"),
                stock = 4163,
                price = 41.028,
                productType = MenuDish.build(code = "Eamon", name = "Criselda"),
                saleStrategy = null
            ),
            Product(
                code = "Kianna2",
                name = "Jerrold 2",
                description = "Richmond",
                imageUrl = "Migdalia",
                measure = Measure(code = "Latishia", value = "Jermichael"),
                currency = Currency(code = "Katharina", value = "Trena"),
                stock = 4163,
                price = 41.028,
                productType = MenuDish.build(code = "Eamon", name = "Criselda"),
                saleStrategy = null
            )
        )
    }

    private fun mockMenuProduct(): Product {
        return Product(
            code = "Nilsa",
            name = "Jamila",
            description = "Tonisha",
            imageUrl = "Cyrstal",
            measure = Measure(code = "Latishia", value = "Jermichael"),
            currency = Currency(code = "Katharina", value = "Trena"),
            stock = 4897,
            price = 48.731,
            productType = MenuDish.build(code = "Eamon", name = "Criselda"),
            saleStrategy = null
        )
    }

    private fun mockCartaProduct(): Product {
        return Product(
            code = "Nilsa",
            name = "Jamila",
            description = "Tonisha",
            imageUrl = "Cyrstal",
            measure = Measure(code = "Latishia", value = "Jermichael"),
            currency = Currency(code = "Katharina", value = "Trena"),
            stock = 4897,
            price = 48.731,
            productType = CartaDish.build(code = "Eamon", name = "Criselda"),
            saleStrategy = null
        )
    }

    private fun createOrderMenuDetail(): OrderDetail {
        return OrderDetail(
            product = mockMenuProduct(),
            quantity = 2,
            saleOrderItemStrategy = null,
            appetizers = arrayListOf()
        )
    }

    private fun createOrderDetail(): OrderDetail {
        return OrderDetail(
            product = mockCartaProduct(),
            quantity = 2,
            saleOrderItemStrategy = null,
            appetizers = arrayListOf()
        )
    }
}