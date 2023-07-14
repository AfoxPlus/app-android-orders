package com.afoxplus.orders.delivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.models.ButtonStateModel
import com.afoxplus.orders.delivery.views.events.AddedProductToCurrentOrderSuccessfullyEvent
import com.afoxplus.orders.delivery.views.extensions.getAmountFormat
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.CalculateSubTotalByProduct
import com.afoxplus.orders.usecases.actions.FindProductInOrder
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val eventBusListener: UIKitEventBusWrapper,
    private val findProductInOrder: FindProductInOrder,
    private val calculateSubTotalByProduct: CalculateSubTotalByProduct,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mButtonSubTotalState: MutableLiveData<ButtonStateModel> by lazy {
        MutableLiveData<ButtonStateModel>(ButtonStateModel.getAddButtonState())
    }

    private val mStateScreen: MutableLiveData<StateScreen> by lazy { MutableLiveData(StateScreen.Add) }

    val product: LiveData<Product> get() = mProduct
    val quantity: LiveData<Int> get() = mQuantity
    val buttonSubTotalState: LiveData<ButtonStateModel> get() = mButtonSubTotalState

    private var quantityChanged: Int = 0

    private val mEventProductAddedToCardSuccess: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<UIKitEvent<Unit>> get() = mEventProductAddedToCardSuccess


    fun startWithProduct(product: Product) = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            setupUpdateScreen(orderDetail)
        } ?: mQuantity.postValue(null)
    }

    private fun setupUpdateScreen(orderDetail: OrderDetail) {
        mStateScreen.value = StateScreen.Update
        setOrderAndVerifyQuantity(orderDetail)
    }

    fun calculateSubTotalByProduct(quantity: Int) =
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            product.value?.let { product ->
                quantityChanged = quantity
                val subTotal = calculateSubTotalByProduct(quantity, product)
                setupStateButtonSubTotal(
                    subTotal.getAmountFormat(), isQuantityEnabled(quantityChanged)
                )
            }
        }

    private fun setOrderAndVerifyQuantity(orderDetail: OrderDetail) {
        val subTotal = orderDetail.calculateSubTotal().getAmountFormat()
        mQuantity.postValue(orderDetail.quantity)
        setupStateButtonSubTotal(subTotal, enabledButton = true)
    }

    fun addOrUpdateToCurrentOrder() =
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mProduct.value?.let { product ->
                addOrUpdateProductToCurrentOrder(quantityChanged, product)
                mEventProductAddedToCardSuccess.postValue(UIKitEvent(Unit))
                eventBusListener.send(AddedProductToCurrentOrderSuccessfullyEvent.build())
            }
        }

    private fun isQuantityEnabled(quantity: Int): Boolean = quantity > 0
    private fun setupStateButtonSubTotal(subTotal: String, enabledButton: Boolean) {
        when (mStateScreen.value) {
            is StateScreen.Update ->
                setButtonSubTotalState(
                    ButtonStateModel.getUpdateButtonState(
                        paramTitle = subTotal,
                        enabledButton
                    )
                )

            else ->
                setButtonSubTotalState(
                    ButtonStateModel.getAddButtonState(
                        paramTitle = subTotal,
                        enabledButton
                    )
                )
        }
    }

    private fun setButtonSubTotalState(buttonStateModel: ButtonStateModel) {
        mButtonSubTotalState.value = buttonStateModel
    }

    sealed interface StateScreen {
        object Add : StateScreen
        object Update : StateScreen
    }

}