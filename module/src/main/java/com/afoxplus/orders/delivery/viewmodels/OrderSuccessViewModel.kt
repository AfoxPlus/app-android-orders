package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OrderSuccessViewModel @Inject constructor(
    private val eventBus: UIKitEventBusWrapper,
    private val coroutineDispatcher: UIKitCoroutineDispatcher
) : ViewModel() {

    val onEventBusListener = eventBus.listen()

    fun clickOnNewOrder() = viewModelScope.launch(coroutineDispatcher.getMainDispatcher()) {
        eventBus.send(GoToNewOrderEvent)
    }
}