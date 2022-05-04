package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.views.events.AddedProductToCurrentOrderSuccessfullyEvent
import com.afoxplus.orders.delivery.views.extensions.getAmountFormat
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.CalculateSubTotalByProduct
import com.afoxplus.orders.usecases.actions.FindProductInOrder
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
    private val mSubTotal: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val mEnableSubTotalButton: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val product: LiveData<Product> get() = mProduct
    val quantity: LiveData<Int> get() = mQuantity
    val subTotal: LiveData<String> get() = mSubTotal
    val enableSubTotalButton: LiveData<Boolean> get() = mEnableSubTotalButton

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
            displaySubTotal(calculateSubTotalByProduct(quantity, product))

        }
    }

    private fun setOrderAndVerifyQuantity(orderDetail: OrderDetail) {
        mSubTotal.value = orderDetail.calculateSubTotal().getAmountFormat()
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

    private fun displaySubTotal(subTotal: Double) {
        if (subTotal > 0.0) {
            mSubTotal.value = subTotal.getAmountFormat()
            mEnableSubTotalButton.value = true
        } else {
            mSubTotal.value = ""
            mEnableSubTotalButton.value = false
        }
    }
}