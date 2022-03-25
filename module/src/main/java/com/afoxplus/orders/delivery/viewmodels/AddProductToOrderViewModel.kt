package com.afoxplus.orders.delivery.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.afoxplus.orders.delivery.views.events.ProductAddedToCartSuccessfullyEvent
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.actions.*
import com.afoxplus.orders.usecases.actions.LessItemProductToDifferentContextOrder
import com.afoxplus.orders.usecases.actions.PlusItemProductToDifferentContextOrder
import com.afoxplus.orders.usecases.actions.SetItemProductInDifferentContextOrder
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.Event
import com.afoxplus.uikit.bus.EventBusListener
import com.afoxplus.uikit.di.UIKitIODispatcher
import com.afoxplus.uikit.di.UIKitMainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val findProductInOrder: FindProductInOrder,
    private val orderEventBus: EventBusListener,
    private val plusItemProduct: PlusItemProductToDifferentContextOrder,
    private val lessItemProduct: LessItemProductToDifferentContextOrder,
    private val setItemProduct: SetItemProductInDifferentContextOrder,
    private val updateProductInDifferentContextOrder: UpdateProductInDifferentContextOrder,
    @UIKitIODispatcher private val ioDispatcher: CoroutineDispatcher,
    @UIKitMainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }

    val product: LiveData<Product> get() = mProduct
    val quantity: LiveData<Int> get() = mQuantity
    val order: LiveData<Order> get() = mOrder

    private val mEventProductAddedToCardSuccess: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<Event<Unit>> get() = mEventProductAddedToCardSuccess

    fun setProduct(product: Product) = viewModelScope.launch(mainDispatcher) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            setItemProduct(product, orderDetail.quantity).collectLatest { result ->
                result.onSuccess { order -> setOrderAndVerifyQuantity(order, product) }
                result.onFailure {
                    Log.d("LOG_VALE", "Error: $it")
                }
            }
        } ?: mQuantity.postValue(null)
    }

    fun plusProductToDifferentContextOrder() = viewModelScope.launch(ioDispatcher) {
        Log.d("LOG_VALE", "Click")
        product.value?.let { product ->
            Log.d("LOG_VALE", "Clicked")
            plusItemProduct(product).collectLatest { result ->
                result.onSuccess { order -> setOrderAndVerifyQuantity(order, product) }
                result.onFailure {
                    Log.d("LOG_VALE", "Error: $it")
                }
            }
        }
    }

    fun lessProductToDifferentContextOrder() = viewModelScope.launch(ioDispatcher) {
        Log.d("LOG_VALE", "Click")
        product.value?.let { product ->
            Log.d("LOG_VALE", "Clicked")
            lessItemProduct(product).collectLatest { result ->
                result.onSuccess { order -> setOrderAndVerifyQuantity(order, product) }
                result.onFailure {
                    Log.d("LOG_VALE", "Error: $it")
                }
            }
        }
    }

    private fun setOrderAndVerifyQuantity(order: Order, product: Product) {
        mOrder.postValue(order)
        val quantity =
            order.getOrderDetails().find { item -> item.product.code == product.code }?.quantity
        mQuantity.postValue(quantity)
    }

    fun addOrUpdateOrder() =
        viewModelScope.launch(ioDispatcher) {
            mProduct.value?.let { product ->
                updateProductInDifferentContextOrder(product).collectLatest { result ->
                    result.onSuccess {
                        mEventProductAddedToCardSuccess.postValue(Event(Unit))
                        orderEventBus.send(ProductAddedToCartSuccessfullyEvent.build(it))
                    }
                    result.onFailure {
                        Log.d("ORDERS", "Error: $it")
                    }
                }
            }
        }
}