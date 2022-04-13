package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.*
import com.afoxplus.orders.delivery.views.events.AddedProductToCurrentOrderSuccessfullyEvent
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.*
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.Event
import com.afoxplus.uikit.bus.EventBusListener
import com.afoxplus.uikit.di.UIKitMainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val eventBusListener: EventBusListener,
    private val findProductInOrder: FindProductInOrder,
    private val calculateSubTotalByProduct: CalculateSubTotalByProduct,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    @UIKitMainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mSubTotal: MutableLiveData<Double> by lazy { MutableLiveData<Double>() }

    val product: LiveData<Product> get() = mProduct
    val quantity: LiveData<Int> get() = mQuantity
    val subTotal: LiveData<Double> get() = mSubTotal
    private var quantityChanged: Int = 0

    private val mEventProductAddedToCardSuccess: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<Event<Unit>> get() = mEventProductAddedToCardSuccess

    fun setProduct(product: Product) = viewModelScope.launch(mainDispatcher) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            setOrderAndVerifyQuantity(orderDetail)
        } ?: mQuantity.postValue(null)
    }

    fun calculateSubTotalByProduct(quantity: Int) = viewModelScope.launch(mainDispatcher) {
        product.value?.let { product ->
            quantityChanged = quantity
            mSubTotal.value = calculateSubTotalByProduct(quantity, product)
        }
    }

    private fun setOrderAndVerifyQuantity(orderDetail: OrderDetail) {
        mSubTotal.value = orderDetail.calculateSubTotal()
        mQuantity.postValue(orderDetail.quantity)
    }

    fun addOrUpdateToCurrentOrder() =
        viewModelScope.launch(mainDispatcher) {
            mProduct.value?.let { product ->
                addOrUpdateProductToCurrentOrder(quantityChanged, product)
                mEventProductAddedToCardSuccess.postValue(Event(Unit))
                eventBusListener.send(AddedProductToCurrentOrderSuccessfullyEvent.build())
            }
        }
}