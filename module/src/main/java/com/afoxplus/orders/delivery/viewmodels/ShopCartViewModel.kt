package com.afoxplus.orders.delivery.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.models.SendOrderStatusUIModel
import com.afoxplus.orders.domain.entities.Client
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.entities.OrderType
import com.afoxplus.orders.cross.exceptions.ExceptionMessage
import com.afoxplus.orders.cross.exceptions.OrderBusinessException
import com.afoxplus.orders.delivery.views.extensions.stringToObject
import com.afoxplus.orders.domain.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.GetRestaurantNameUseCase
import com.afoxplus.orders.domain.usecases.GetRestaurantPaymentsUseCase
import com.afoxplus.orders.domain.usecases.SendOrderUseCase
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.common.ResultState
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.afoxplus.uikit.objects.vendor.Vendor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShopCartViewModel @Inject constructor(
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrderUseCase,
    private val getCurrentOrder: GetCurrentOrderUseCase,
    private val deleteProductToCurrentOrder: DeleteProductToCurrentOrderUseCase,
    private val sendOrder: SendOrderUseCase,
    private val getRestaurantName: GetRestaurantNameUseCase,
    private val getRestaurantPaymentsUseCase: GetRestaurantPaymentsUseCase,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mOrder: MutableLiveData<Order> by lazy { MutableLiveData<Order>() }
    val order: LiveData<Order> get() = mOrder

    private val nameButtonSendOrderMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val nameButtonSendOrderLiveData: LiveData<String> get() = nameButtonSendOrderMutableLiveData

    private val mEventOnBackSendOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventOnBackSendOrder: LiveData<UIKitEvent<Unit>> get() = mEventOnBackSendOrder

    private val mEventOpenAdditionalInfo: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val evenOpenAdditionalInfo: LiveData<UIKitEvent<Unit>> get() = mEventOpenAdditionalInfo

    private val mEventOpenScan: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventOpenScan: LiveData<UIKitEvent<Unit>> get() = mEventOpenScan

    private val mEventValidateTableOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventValidateTableOrder: LiveData<UIKitEvent<Unit>> get() = mEventValidateTableOrder

    private val mEventRemoveTableOrder: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventRemoveTableOrder: LiveData<UIKitEvent<Unit>> get() = mEventRemoveTableOrder

    private val mErrorClientNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientNameLiveData: LiveData<String> get() = mErrorClientNameMutableLiveData

    private val mErrorClientPhoneNumberMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorClientPhoneNumberLiveData: LiveData<String> get() = mErrorClientPhoneNumberMutableLiveData

    private val mEventSuccessOrder: MutableLiveData<SendOrderStatusUIModel> by lazy { MutableLiveData<SendOrderStatusUIModel>() }
    val eventOpenSuccessOrder: LiveData<SendOrderStatusUIModel> get() = mEventSuccessOrder

    private val mButtonSendLoading: MutableLiveData<UIKitEvent<Boolean>> by lazy { MutableLiveData() }
    val buttonSendLoading: LiveData<UIKitEvent<Boolean>> get() = mButtonSendLoading
    private val mGoToAddCardProductEvent: MutableLiveData<UIKitEvent<Product>> by lazy { MutableLiveData<UIKitEvent<Product>>() }
    val goToAddCardProductEvent: LiveData<UIKitEvent<Product>> get() = mGoToAddCardProductEvent

    private var paymentMethods: MutableList<PaymentMethod> = mutableListOf()

    private val mPaymentMethodSelectedMutableLiveData: MutableLiveData<PaymentMethod> by lazy { MutableLiveData<PaymentMethod>() }
    val paymentMethodSelectedLiveData: LiveData<PaymentMethod> get() = mPaymentMethodSelectedMutableLiveData

    private val mRetrySize: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }

    fun loadData() {
        loadCurrentOrder()
        loadPaymentMethods()
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            paymentMethods = getRestaurantPaymentsUseCase.invoke().toMutableList()
            if (paymentMethods.isNotEmpty()) {
                mPaymentMethodSelectedMutableLiveData.postValue(paymentMethods.first())
            }
        }
    }

    private fun loadCurrentOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            getCurrentOrder().collect {
                if (it != null && it.getOrderDetails().isNotEmpty())
                    mOrder.postValue(it)
                else
                    closeScreen()
            }
        }
    }

    private fun closeScreen() {
        mEventOnBackSendOrder.postValue(UIKitEvent(Unit))
    }

    private fun onClickSendOrder() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mEventValidateTableOrder.postValue(UIKitEvent(Unit))
    }

    fun deleteItem(orderDetail: OrderDetail) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            deleteProductToCurrentOrder(orderDetail.product)
        }
    }

    fun updateQuantity(orderDetail: OrderDetail, quantity: Int) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            addOrUpdateProductToCurrentOrder(quantity, orderDetail.product)
        }
    }

    fun editMenuDish(orderDetail: OrderDetail) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mGoToAddCardProductEvent.postValue(UIKitEvent(orderDetail.product))
        }
    }

    fun handleClickSender(isOrderDetailView: Boolean) {
        if (isOrderDetailView) {
            if (mOrder.value?.isValidOrderType() == true) {
                mEventOpenAdditionalInfo.postValue(UIKitEvent(Unit))
                nameButtonSendOrderMutableLiveData.postValue(mOrder.value?.getLabelSendMyOrder())
            } else {
                mEventOpenScan.postValue(UIKitEvent(Unit))
            }
        } else
            onClickSendOrder()
    }

    fun handleBackPressed(isAdditionalInformation: Boolean) {
        if (isAdditionalInformation) {
            mEventRemoveTableOrder.postValue(UIKitEvent(Unit))
            nameButtonSendOrderMutableLiveData.postValue("Continuar")
        } else
            closeScreen()
    }

    private fun getOrderType(): OrderType = mOrder.value?.orderType ?: OrderType.Delivery

    private fun sendOrder() = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mOrder.value?.let { order ->
            changeButtonSendEnable(false)
            val result = sendOrder.invoke(order)
            handleOrderResult(result)
        }
    }

    fun retrySendOrder(delayTime: Long = RETRY_DELAY) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mRetrySize.value?.let { retrySize ->
                mRetrySize.value = retrySize.plus(1)
                delay(delayTime)
                if (retrySize <= LIMIT_RETRY) {
                    sendOrder()
                } else handleBusinessExceptionRetry()
            }
        }
    }

    private fun handleBusinessExceptionRetry() {
        mEventSuccessOrder.postValue(
            SendOrderStatusUIModel.Error(
                OrderBusinessException(
                    contentMessage = ExceptionMessage(
                        value = "Hubo un problema al enviar el pedido",
                        info = "Ha ocurrido un problema al enviar el pedido, intentalo nuevamente"
                    )
                )
            )
        )
    }

    fun setClientToOrder(client: Client) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            if (validateClient(client, getOrderType())) {
                mOrder.value?.also { order ->
                    order.client = client
                    order.paymentMethod = mPaymentMethodSelectedMutableLiveData.value
                }
                sendOrder()
            }
        }
    }

    fun changeButtonSendEnable(isEnable: Boolean) {
        mButtonSendLoading.value = UIKitEvent(isEnable)
    }

    private fun handleOrderResult(result: ResultState<String>) {
        when (result) {
            is ResultState.Success -> {
                mEventSuccessOrder.postValue(SendOrderStatusUIModel.Success(result.data))
            }

            is ResultState.Error -> {
                mEventSuccessOrder.postValue(SendOrderStatusUIModel.Error(result.exception))
            }
        }
    }

    fun restaurantName(): String = getRestaurantName()

    fun fetchPaymentMethods(): List<PaymentMethod> = paymentMethods

    private fun validateClient(client: Client, orderType: OrderType?): Boolean {
        when (orderType) {
            OrderType.Local -> {
                if (TextUtils.isEmpty(client.name)) {
                    mErrorClientNameMutableLiveData.postValue("El nombre es obligatorio.")
                    return false
                }
            }

            OrderType.Delivery -> {
                if (TextUtils.isEmpty(client.name)) {
                    mErrorClientNameMutableLiveData.postValue("El nombre es obligatorio.")
                    return false
                }
                if (TextUtils.isEmpty(client.phone)) {
                    mErrorClientPhoneNumberMutableLiveData.postValue("El telefono es obligatorio.")
                    return false
                }
            }

            else -> {
                //Nothing
            }
        }
        return true
    }

    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        mPaymentMethodSelectedMutableLiveData.postValue(paymentMethod)
        paymentMethods.forEach {
            it.isSelected = paymentMethod.id == it.id
        }
    }

    fun setOrderTypeFromScan(data: String) {
        try {
            val vendor = stringToObject<Vendor>(data)
            mOrder.value?.let { order ->
                if (order.restaurantId == vendor.restaurantId) {
                    mOrder.value?.orderType?.description = vendor.tableId
                    handleClickSender(true)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        private const val LIMIT_RETRY = 3
        private const val RETRY_DELAY = 2000L
    }
}