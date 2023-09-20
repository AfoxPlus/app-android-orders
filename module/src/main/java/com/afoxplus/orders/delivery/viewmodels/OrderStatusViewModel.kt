package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.models.OrderStateUIModel
import com.afoxplus.orders.domain.usecases.OrderStatusUseCase
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class OrderStatusViewModel @Inject constructor(
    private val useCase: OrderStatusUseCase,
    private val dispatcher: UIKitCoroutineDispatcher
) : ViewModel() {

    private val _orders: MutableLiveData<OrderStateUIModel> by lazy { MutableLiveData<OrderStateUIModel>() }
    val orders: LiveData<OrderStateUIModel> get() = _orders

    fun fetchStateOrders() = viewModelScope.launch(dispatcher.getIODispatcher()) {
        try {
            _orders.postValue(OrderStateUIModel.Loading)
            val orders = useCase.invoke()
            _orders.postValue(OrderStateUIModel.Success(orders))
        } catch (e: Exception) {
            _orders.postValue(OrderStateUIModel.Error(e))
        }
    }

}