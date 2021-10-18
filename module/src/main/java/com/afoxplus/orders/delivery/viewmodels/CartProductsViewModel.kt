package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.entities.Order
import com.afoxplus.uikit.bus.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CartProductsViewModel @Inject constructor() : ViewModel() {

    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }
    val order: LiveData<Order> get() = mOrder

    private val mEventOnClickSendOrder: MutableLiveData<Event<Unit>> by lazy { MutableLiveData<Event<Unit>>() }
    val eventOnClickSendOrder: LiveData<Event<Unit>> get() = mEventOnClickSendOrder

    val nameButtonSendOrder: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun onClickSendOrder() = viewModelScope.launch(Dispatchers.Main) {
        mEventOnClickSendOrder.postValue(Event(Unit))
    }

    fun setOrder(order: Order) = mOrder.postValue(order)


}