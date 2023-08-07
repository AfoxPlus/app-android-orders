package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.views.events.GoToHomeEvent
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OrderSuccessViewModel @Inject constructor(
    private val eventBusWrapper: UIKitEventBusWrapper,
    private val coroutineDispatcher: UIKitCoroutineDispatcher
) : ViewModel() {

    val onEventBusListener = eventBusWrapper.listen()

    fun clickOnNewOrder() = viewModelScope.launch(coroutineDispatcher.getMainDispatcher()) {
        eventBusWrapper.send(GoToNewOrderEvent)
    }

    fun clickGoToHome(): Job {
        return viewModelScope.launch(coroutineDispatcher.getMainDispatcher()) {
            eventBusWrapper.send(GoToHomeEvent)
        }
    }

}