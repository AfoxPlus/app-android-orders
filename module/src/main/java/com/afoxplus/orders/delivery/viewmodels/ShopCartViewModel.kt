package com.afoxplus.orders.delivery.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.DeleteProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.GetCurrentOrder
import com.afoxplus.orders.usecases.actions.SendOrder
import com.afoxplus.uikit.bus.Event
import com.afoxplus.uikit.di.UIKitMainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShopCartViewModel @Inject constructor(
    @UIKitMainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    private val getCurrentOrder: GetCurrentOrder,
    private val deleteProductToCurrentOrder: DeleteProductToCurrentOrder,
    private val sendOrder: SendOrder
) : ViewModel() {

    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }
    val order: LiveData<Order> get() = mOrder

    private val mEventOnClickSendOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventOnClickSendOrder: LiveData<Event<Unit>> get() = mEventOnClickSendOrder

    private val nameButtonSendOrderMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val nameButtonSendOrderLiveData: LiveData<String> get() = nameButtonSendOrderMutableLiveData

    private val mEventOnBackSendOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventOnBackSendOrder: LiveData<Event<Unit>> get() = mEventOnBackSendOrder

    private val mEventOpenTableOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventOpenTableOrder: LiveData<Event<Unit>> get() = mEventOpenTableOrder

    private val mEventRemoveTableOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventRemoveTableOrder: LiveData<Event<Unit>> get() = mEventRemoveTableOrder

    private val mErrorClientNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientNameLiveData: LiveData<String> get() = mErrorClientNameMutableLiveData

    private val mErrorClientPhoneNumberMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientPhoneNumberLiveData: LiveData<String> get() = mErrorClientPhoneNumberMutableLiveData

    private val mEventSuccessOrder: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }
    val eventOpenSuccessOrder: LiveData<Event<String>> get() = mEventSuccessOrder

    init {
        loadCurrentOrder()
    }

    private fun loadCurrentOrder() {
        viewModelScope.launch(mainDispatcher) {
            getCurrentOrder().collect {
                if (it != null && it.getOrderDetails().isNotEmpty())
                    mOrder.postValue(it)
                else
                    closeScreen()
            }
        }
    }

    private fun closeScreen() {
        mEventOnBackSendOrder.postValue(Event(Unit))
    }

    private fun onClickSendOrder() = viewModelScope.launch(mainDispatcher) {
        mEventOnClickSendOrder.postValue(Event(Unit))
    }

    fun deleteItem(orderDetail: OrderDetail) {
        viewModelScope.launch(mainDispatcher) {
            deleteProductToCurrentOrder.invoke(orderDetail.product)
        }
    }

    fun updateQuantity(orderDetail: OrderDetail, quantity: Int) {
        viewModelScope.launch(mainDispatcher) {
            addOrUpdateProductToCurrentOrder.invoke(quantity, orderDetail.product)
        }
    }

    fun handleClickSender(isOrderCartView: Boolean) {
        if (isOrderCartView) {
            mEventOpenTableOrder.postValue(Event(Unit))
            nameButtonSendOrderMutableLiveData.postValue(mOrder.value?.getLabelSendMyOrder())
        } else
            onClickSendOrder()
    }

    fun handleBackPressed(isTableOrder: Boolean) {
        if (isTableOrder) {
            mEventRemoveTableOrder.postValue(Event(Unit))
            nameButtonSendOrderMutableLiveData.postValue("Continuar")
        } else
            closeScreen()
    }

    fun sendOrder(tableNumber: String, clientName: String, clientPhone: String) {
        if (validateClient(clientName, clientPhone)) {
            val order = mOrder.value?.also {
                it.tableNumber = tableNumber
                it.clientName = clientName
                it.clientPhoneNumber = clientPhone
            }
            if (order != null)
                viewModelScope.launch(mainDispatcher) {
                    val message = sendOrder.invoke(order)
                    openSuccessOrder(message)
                }
        }
    }

    private fun openSuccessOrder(message: String) {
        mEventSuccessOrder.postValue(Event(message))
    }

    private fun validateClient(clientName: String, clientPhone: String): Boolean {
        if (TextUtils.isEmpty(clientName)) {
            mErrorClientNameMutableLiveData.postValue("El nombre es obligatorio.")
            return false
        }
        if (TextUtils.isEmpty(clientPhone)) {
            mErrorClientPhoneNumberMutableLiveData.postValue("El telefono es obligatorio.")
            return false
        }
        return true
    }
}