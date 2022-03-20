package com.afoxplus.orders.delivery.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.afoxplus.orders.delivery.views.events.ProductAddedToCartSuccessfullyEvent
import com.afoxplus.orders.usecases.actions.AddProductToOrder
import com.afoxplus.orders.usecases.actions.FindProductInOrder
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
    private val addProductToOrderUseCase: AddProductToOrder,
    private val findProductInOrder: FindProductInOrder,
    private val orderEventBus: EventBusListener,
    @UIKitIODispatcher private val ioDispatcher: CoroutineDispatcher,
    @UIKitMainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    val product: LiveData<Product> get() = mProduct
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val quantity: LiveData<Int> get() = mQuantity

    private val mEventProductAddedToCardSuccess: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<Event<Unit>> get() = mEventProductAddedToCardSuccess

    fun setProduct(product: Product) = viewModelScope.launch(mainDispatcher) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            mQuantity.postValue(orderDetail.quantity)
        } ?: mQuantity.postValue(null)
    }

    fun addProductToOrder() =
        viewModelScope.launch(ioDispatcher) {
            mProduct.value?.let { product ->
                //TODO: update parameter quantity
                Log.d("ORDERS", "Loading:...")
                addProductToOrderUseCase(product, 1).collectLatest { result ->
                    result.onSuccess {
                        mEventProductAddedToCardSuccess.postValue(Event(Unit))
                        orderEventBus.send(ProductAddedToCartSuccessfullyEvent.build(it))
                    }
                    result.onFailure {
                        Log.d("ORDERS", "Error: ${it}")
                    }
                }
            }

        }
}