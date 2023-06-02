package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OrderSuccessViewModel @Inject constructor(
    private val eventBus: UIKitEventBusWrapper,
    private val coroutineDispatcher: UIKitCoroutineDispatcher
) : ViewModel() {


    val eventOnNewOrder = eventBus.getBusEventFlow()
        .filter { item -> item is GoToNewOrderEvent }
        .shareIn(scope = viewModelScope, started = SharingStarted.Eagerly)

    fun clickOnNewOrder() = viewModelScope.launch(coroutineDispatcher.getMainDispatcher()) {
        eventBus.send(GoToNewOrderEvent.build())
    }
}