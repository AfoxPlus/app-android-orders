package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.GetCurrentOrderFlow
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
    private val getCurrentOrderFlow: GetCurrentOrderFlow
) : ViewModel() {

    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }
    val order: LiveData<Order> get() = mOrder

    private val mEventOnClickSendOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventOnClickSendOrder: LiveData<Event<Unit>> get() = mEventOnClickSendOrder

    private val nameButtonSendOrderMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val nameButtonSendOrderLiveData: LiveData<String> get() = nameButtonSendOrderMutableLiveData

    init {
        loadCurrentOrder()
    }

    private fun loadCurrentOrder() {
        viewModelScope.launch(mainDispatcher) {
            getCurrentOrderFlow().collect {
                println("Here is order update")
                if (it != null) {
                    mOrder.postValue(it)
                    println("Here is order update: ${it.getTotalWithFormat()}")
                } else
                    closeScreen()
            }
        }
    }

    private fun closeScreen() {

    }

    fun onClickSendOrder() = viewModelScope.launch(mainDispatcher) {
        mEventOnClickSendOrder.postValue(Event(Unit))
    }

    fun setOrder(order: Order) {
        // mOrder.postValue(order)
    }


    fun deleteItem(orderDetail: OrderDetail) {

    }

    fun updateQuantity(orderDetail: OrderDetail, quantity: Int) {
        viewModelScope.launch(mainDispatcher) {
            addOrUpdateProductToCurrentOrder.invoke(quantity, orderDetail.product)
        }
    }
}