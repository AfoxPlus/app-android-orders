package com.afoxplus.orders.delivery.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.entities.Client
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.entities.OrderType
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.DeleteProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.GetCurrentOrder
import com.afoxplus.orders.usecases.actions.GetRestaurantName
import com.afoxplus.orders.usecases.actions.SendOrder
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShopCartViewModel @Inject constructor(
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    private val getCurrentOrder: GetCurrentOrder,
    private val deleteProductToCurrentOrder: DeleteProductToCurrentOrder,
    private val sendOrder: SendOrder,
    private val getRestaurantName: GetRestaurantName,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }
    val order: LiveData<Order> get() = mOrder

    private val nameButtonSendOrderMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val nameButtonSendOrderLiveData: LiveData<String> get() = nameButtonSendOrderMutableLiveData

    private val mEventOnBackSendOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventOnBackSendOrder: LiveData<UIKitEvent<Unit>> get() = mEventOnBackSendOrder

    private val mEventOpenTableOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventOpenTableOrder: LiveData<UIKitEvent<Unit>> get() = mEventOpenTableOrder

    private val mEventValidateTableOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventValidateTableOrder: LiveData<UIKitEvent<Unit>> get() = mEventValidateTableOrder

    private val mEventRemoveTableOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventRemoveTableOrder: LiveData<UIKitEvent<Unit>> get() = mEventRemoveTableOrder

    private val mErrorClientNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientNameLiveData: LiveData<String> get() = mErrorClientNameMutableLiveData

    private val mErrorClientPhoneNumberMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientPhoneNumberLiveData: LiveData<String> get() = mErrorClientPhoneNumberMutableLiveData

    private val mEventSuccessOrder: MutableLiveData<UIKitEvent<String>> by lazy { MutableLiveData<UIKitEvent<String>>() }
    val eventOpenSuccessOrder: LiveData<UIKitEvent<String>> get() = mEventSuccessOrder

    private val mButtonSendLoading: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData() }
    val buttonSendLoading: LiveData<UIKitEvent<Unit>> get() = mButtonSendLoading
    private val mGoToAddCardProductEvent: MutableLiveData<UIKitEvent<Product>> by lazy { MutableLiveData<UIKitEvent<Product>>() }
    val goToAddCardProductEvent: LiveData<UIKitEvent<Product>> get() = mGoToAddCardProductEvent

    init {
        loadCurrentOrder()
    }

    private fun loadCurrentOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            getCurrentOrder().collect {
                if (it != null && it.getOrderDetails().isNotEmpty())
                    mOrder.postValue(it)
                else
                    closeScreen()
            }
        }
    }

    private fun closeScreen() {
        mEventOnBackSendOrder.postValue(UIKitEvent(Unit))
    }

    private fun onClickSendOrder() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mEventValidateTableOrder.postValue(UIKitEvent(Unit))
    }

    fun deleteItem(orderDetail: OrderDetail) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            deleteProductToCurrentOrder(orderDetail.product)
        }
    }

    fun updateQuantity(orderDetail: OrderDetail, quantity: Int) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            addOrUpdateProductToCurrentOrder(quantity, orderDetail.product)
        }
    }

    fun editMenuDish(orderDetail: OrderDetail) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mGoToAddCardProductEvent.postValue(UIKitEvent(orderDetail.product))
        }
    }

    fun handleClickSender(isOrderCartView: Boolean) {
        if (isOrderCartView) {
            mEventOpenTableOrder.postValue(UIKitEvent(Unit))
            nameButtonSendOrderMutableLiveData.postValue(mOrder.value?.getLabelSendMyOrder())
        } else
            onClickSendOrder()

    }

    fun handleBackPressed(isTableOrder: Boolean) {
        if (isTableOrder) {
            mEventRemoveTableOrder.postValue(UIKitEvent(Unit))
            nameButtonSendOrderMutableLiveData.postValue("Continuar")
        } else
            closeScreen()
    }

    private fun getOrderType(): OrderType = mOrder.value?.orderType ?: OrderType.Delivery
    fun sendOrder(client: Client) {
        if (validateClient(client, getOrderType())) {
            mOrder.value?.also { order ->
                order.client = client
                viewModelScope.launch(coroutines.getMainDispatcher()) {
                    mButtonSendLoading.value = UIKitEvent(Unit)
                    val message = sendOrder.invoke(order)
                    openSuccessOrder(message)
                }
            }
        }
    }

    fun restaurantName(): String = getRestaurantName()
    private fun openSuccessOrder(message: String) {
        mEventSuccessOrder.postValue(UIKitEvent(message))
    }

    private fun validateClient(client: Client, orderType: OrderType?): Boolean {
        when (orderType) {
            OrderType.Local -> {
                if (TextUtils.isEmpty(client.name)) {
                    mErrorClientNameMutableLiveData.postValue("El nombre es obligatorio.")
                    return false
                }
            }

            OrderType.Delivery -> {
                if (TextUtils.isEmpty(client.name)) {
                    mErrorClientNameMutableLiveData.postValue("El nombre es obligatorio.")
                    return false
                }
                if (TextUtils.isEmpty(client.phone)) {
                    mErrorClientPhoneNumberMutableLiveData.postValue("El telefono es obligatorio.")
                    return false
                }
            }

            else -> {
                //Nothing
            }
        }
        return true
    }
}