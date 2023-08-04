package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.actions.ClearCurrentOrder
import com.afoxplus.orders.usecases.actions.GetCurrentOrder
import com.afoxplus.orders.usecases.actions.GetRestaurantName
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MarketOrderViewModel @Inject constructor(
    private val eventBusListener: UIKitEventBusWrapper,
    private val clearCurrentOrder: ClearCurrentOrder,
    private val getCurrentOrder: GetCurrentOrder,
    private val getRestaurantName: GetRestaurantName,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    val onEventBusListener = eventBusListener.listen()
    private val mOnMarketOrderEvent: MutableSharedFlow<MarketOrderEvent> by lazy { MutableSharedFlow() }
    val onMarketOrderEvent: SharedFlow<MarketOrderEvent> get() = mOnMarketOrderEvent

    private val mOrder: MutableLiveData<Order?> by lazy { MutableLiveData<Order?>() }
    val order: LiveData<Order?> get() = mOrder

    init {
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

    fun restaurantName(): String = getRestaurantName()

    fun onClickViewOrder() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mOrder.value?.let { order ->
            mOnMarketOrderEvent.emit(MarketOrderEvent.OnClickViewOrder(order = order))
        }
    }

    fun onBackPressed() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        clearCurrentOrder()
        mOnMarketOrderEvent.emit(MarketOrderEvent.OnBackPressed)
    }

    sealed class MarketOrderEvent {
        data class OnClickViewOrder(val order: Order) : MarketOrderEvent()
        object OnBackPressed : MarketOrderEvent()
    }

}