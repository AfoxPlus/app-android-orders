package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.usecases.ClearCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetCurrentOrderUseCase
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
    private val clearCurrentOrder: ClearCurrentOrderUseCase,
    private val getCurrentOrder: GetCurrentOrderUseCase,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {
    val onEventBusListener = eventBusListener.listen()
    private val mOnMarketOrderEvent: MutableSharedFlow<MarketOrderEvent> by lazy { MutableSharedFlow() }
    val onMarketOrderEvent: SharedFlow<MarketOrderEvent> get() = mOnMarketOrderEvent

    private val mOrder: MutableLiveData<Order?> by lazy { MutableLiveData<Order?>() }
    val order: LiveData<Order?> get() = mOrder

    private val mDisplayOrderModalLiveData: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val displayOrderModalLiveData: LiveData<Unit> get() = mDisplayOrderModalLiveData

    fun loadCurrentOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            getCurrentOrder().collect {
                if (it != null)
                    mOrder.postValue(it)
            }
        }
    }

    fun onClickViewOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mOrder.value?.let { order ->
                mOnMarketOrderEvent.emit(MarketOrderEvent.OnClickViewOrder(order = order))
            }
        }
    }

    fun onBackPressed() = validateBackAction()

    private fun validateBackAction() {
        if (order.value?.isOrderEmpty() == false) {
            mDisplayOrderModalLiveData.postValue(Unit)
        } else {
            clearOrderAndGoBack()
        }
    }

    fun clearOrderAndGoBack() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            clearCurrentOrder()
            mOnMarketOrderEvent.emit(MarketOrderEvent.OnBackPressed)
        }
    }

    sealed class MarketOrderEvent {
        data class OnClickViewOrder(val order: Order) : MarketOrderEvent()
        object OnBackPressed : MarketOrderEvent()
    }

}