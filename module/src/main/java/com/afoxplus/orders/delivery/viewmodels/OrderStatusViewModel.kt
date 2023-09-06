package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.models.OrderStateUIModel
import com.afoxplus.orders.usecases.OrderStatusUseCase
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import com.afoxplus.uikit.result.ResultState
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
        _orders.postValue(OrderStateUIModel.Loading)
        when (val response = useCase.getStatusOrders()) {
            is ResultState.Success -> {
                _orders.postValue(OrderStateUIModel.Success(response.data))
            }

            is ResultState.Error -> {
                _orders.postValue(OrderStateUIModel.Error(Exception()))
            }

            else -> {
                //Nothing
            }
        }
    }
}