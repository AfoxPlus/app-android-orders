package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.actions.ClearCurrentOrder
import com.afoxplus.orders.usecases.actions.GetCurrentOrder
import com.afoxplus.products.delivery.views.events.OnClickProductSaleEvent
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MarketOrderViewModel @Inject constructor(
    private val eventBusListener: UIKitEventBusWrapper,
    private val clearCurrentOrder: ClearCurrentOrder,
    private val getCurrentOrder: GetCurrentOrder,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mGoToAddCardProductEvent: MutableLiveData<UIKitEvent<Product>> by lazy { MutableLiveData<UIKitEvent<Product>>() }
    private val mOrder: MutableLiveData<Order?> by lazy { MutableLiveData<Order?>() }
    private val mEventOnClickViewOrder: MutableLiveData<UIKitEvent<Order>> by lazy { MutableLiveData<UIKitEvent<Order>>() }
    private val mEventOnBackPressed: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }


    val eventOnNewOrder =
        eventBusListener.getBusEventFlow().filter { event -> event is GoToNewOrderEvent }
            .shareIn(scope = viewModelScope, started = SharingStarted.Eagerly)

    val goToAddCardProductEvent: LiveData<UIKitEvent<Product>> get() = mGoToAddCardProductEvent
    val order: LiveData<Order?> get() = mOrder
    val eventOnClickViewOrder: LiveData<UIKitEvent<Order>> get() = mEventOnClickViewOrder
    val eventOnBackPressed: LiveData<UIKitEvent<Unit>> get() = mEventOnBackPressed

    init {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            eventBusListener.getBusEventFlow().collectLatest { event ->
                if (event is OnClickProductSaleEvent) {
                    mGoToAddCardProductEvent.postValue(UIKitEvent(event.product))
                }
            }
        }
        loadCurrentOrder()
    }

    private fun loadCurrentOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            getCurrentOrder().collect {
                if (it != null)
                    mOrder.postValue(it)
            }
        }
    }

    fun onClickViewOrder() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mOrder.value?.let { order ->
            mEventOnClickViewOrder.postValue(UIKitEvent(order))
        }
    }

    fun onBackPressed() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        clearCurrentOrder()
        mEventOnBackPressed.value = UIKitEvent(Unit)
    }
}