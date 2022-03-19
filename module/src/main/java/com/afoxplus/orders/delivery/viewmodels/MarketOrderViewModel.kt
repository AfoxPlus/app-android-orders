package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.*
import com.afoxplus.orders.delivery.views.events.ProductAddedToCartSuccessfullyEvent
import com.afoxplus.orders.entities.Order
import com.afoxplus.products.delivery.views.events.OnClickProductSaleEvent
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.Event
import com.afoxplus.uikit.bus.EventBusListener
import com.afoxplus.uikit.di.UIKitMainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MarketOrderViewModel @Inject constructor(
    private val eventBusListener: EventBusListener,
    @UIKitMainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mGoToAddCardProductEvent: MutableLiveData<Event<Product>> by lazy { MutableLiveData<Event<Product>>() }
    val goToAddCardProductEvent: LiveData<Event<Product>> get() = mGoToAddCardProductEvent
    private val mOrder: MutableLiveData<Order?> by lazy { MutableLiveData<Order?>() }
    val order: LiveData<Order?> get() = mOrder
    private val mEventOnClickViewOrder: MutableLiveData<Event<Order>> by lazy { MutableLiveData<Event<Order>>() }
    val eventOnClickViewOrder: LiveData<Event<Order>> get() = mEventOnClickViewOrder

    init {
        viewModelScope.launch(mainDispatcher) {
            eventBusListener.subscribe().collectLatest { event ->
                if (event is OnClickProductSaleEvent) {
                    mGoToAddCardProductEvent.postValue(Event(event.product))
                }
            }
        }

        viewModelScope.launch(mainDispatcher) {
            eventBusListener.subscribe().collectLatest { event ->
                if (event is ProductAddedToCartSuccessfullyEvent) {
                    mOrder.postValue(event.order)
                }
            }
        }
    }

    fun onClickViewOrder() = viewModelScope.launch(mainDispatcher) {
        mOrder.value?.let { order ->
            mEventOnClickViewOrder.postValue(Event(order))
        }
    }
}