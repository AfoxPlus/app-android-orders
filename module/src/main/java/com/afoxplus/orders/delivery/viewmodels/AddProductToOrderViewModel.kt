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
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val eventBusListener: UIKitEventBusWrapper,
    private val findProductInOrder: FindProductInOrder,
    private val calculateSubTotalByProduct: CalculateSubTotalByProduct,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mSubTotal: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val mEnableSubTotalButton: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val product: LiveData<Product> get() = mProduct
    val quantity: LiveData<Int> get() = mQuantity
    val subTotal: LiveData<String> get() = mSubTotal
    val enableSubTotalButton: LiveData<Boolean> get() = mEnableSubTotalButton

    private var quantityChanged: Int = 0

    private val mEventProductAddedToCardSuccess: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<UIKitEvent<Unit>> get() = mEventProductAddedToCardSuccess

    fun setProduct(product: Product) = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            setOrderAndVerifyQuantity(orderDetail)
        } ?: mQuantity.postValue(null)
    }

    fun calculateSubTotalByProduct(quantity: Int) =
        viewModelScope.launch(coroutines.getMainDispatcher()) {
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
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mProduct.value?.let { product ->
                addOrUpdateProductToCurrentOrder(quantityChanged, product)
                mEventProductAddedToCardSuccess.postValue(UIKitEvent(Unit))
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