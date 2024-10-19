package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.ViewModel
import com.afoxplus.orders.domain.usecases.VendorShareUseCase
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LandingEstablishmentViewModel @Inject constructor(
    private val eventBusListener: UIKitEventBusWrapper,
    private val vendorShareUseCase: VendorShareUseCase,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    fun restaurantName(): String = vendorShareUseCase.getRestaurantName()

}