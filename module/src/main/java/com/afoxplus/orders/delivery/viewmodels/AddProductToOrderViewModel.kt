package com.afoxplus.orders.delivery.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.orders.delivery.models.AppetizerStateModel
import com.afoxplus.orders.delivery.models.ButtonStateModel
import com.afoxplus.orders.delivery.views.events.AddedProductToCurrentOrderSuccessfullyEvent
import com.afoxplus.orders.delivery.views.extensions.getAmountFormat
import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.usecases.actions.AddOrUpdateAppetizerToCurrentOrder
import com.afoxplus.orders.usecases.actions.AddOrUpdateProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.CalculateSubTotalByProduct
import com.afoxplus.orders.usecases.actions.ClearAppetizersOrder
import com.afoxplus.orders.usecases.actions.DeleteProductToCurrentOrder
import com.afoxplus.orders.usecases.actions.FindProductInOrder
import com.afoxplus.orders.usecases.actions.MatchAppetizersByOrder
import com.afoxplus.products.entities.Product
import com.afoxplus.products.usecases.actions.FetchAppetizerByCurrentRestaurant
import com.afoxplus.uikit.bus.UIKitEvent
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.customview.quantitybutton.ButtonEnableType
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val eventBusWrapper: UIKitEventBusWrapper,
    private val findProductInOrder: FindProductInOrder,
    private val calculateSubTotalByProduct: CalculateSubTotalByProduct,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrder,
    private val fetchAppetizerByCurrentRestaurant: FetchAppetizerByCurrentRestaurant,
    private val matchAppetizersByOrder: MatchAppetizersByOrder,
    private val addOrUpdateAppetizerToCurrentOrder: AddOrUpdateAppetizerToCurrentOrder,
    private val deleteProductToCurrentOrder: DeleteProductToCurrentOrder,
    private val clearAppetizersOrder: ClearAppetizersOrder,
    private val coroutines: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>() }
    private val mQuantity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val mButtonSubTotalState: MutableLiveData<ButtonStateModel> by lazy {
        MutableLiveData<ButtonStateModel>(ButtonStateModel.getAddButtonState())
    }
    private val mAppetizersStateModel: MutableLiveData<List<AppetizerStateModel>> by lazy {
        MutableLiveData<List<AppetizerStateModel>>()
    }
    private val mAppetizerVisibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(View.VISIBLE)
    }

    private val mStateScreen: MutableLiveData<StateScreen> by lazy { MutableLiveData(StateScreen.Add) }

    val product: LiveData<Product> get() = mProduct
    val appetizersStateModel: LiveData<List<AppetizerStateModel>> get() = mAppetizersStateModel
    val quantity: LiveData<Int> get() = mQuantity
    val buttonSubTotalState: LiveData<ButtonStateModel> get() = mButtonSubTotalState
    val appetizerVisibility: LiveData<Int> get() = mAppetizerVisibility

    private var quantityChanged: Int = 0

    private var appetizers: List<Product> = arrayListOf()

    private val mEventProductAddedToCardSuccess: MutableLiveData<UIKitEvent<Unit>> by lazy { MutableLiveData<UIKitEvent<Unit>>() }
    val eventProductAddedToCardSuccess: LiveData<UIKitEvent<Unit>> get() = mEventProductAddedToCardSuccess

    fun startWithProduct(product: Product) = viewModelScope.launch(coroutines.getMainDispatcher()) {
        mProduct.postValue(product)
        findProductInOrder(product)?.let { orderDetail ->
            setupUpdateScreen(orderDetail)
        } ?: mQuantity.postValue(null)
        handleProductType(product)
    }

    private fun handleProductType(product: Product) {
        val appetizerVisibility = if (product.isMenuDishType()) View.VISIBLE else View.GONE
        mAppetizerVisibility.postValue(appetizerVisibility)
        if (product.isMenuDishType())
            fetchAppetizerByCurrentRestaurant()
    }

    private fun setupUpdateScreen(orderDetail: OrderDetail) {
        mStateScreen.value = StateScreen.Update
        setOrderAndVerifyQuantity(orderDetail)
    }

    fun calculateSubTotalByProduct(quantity: Int) =
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            product.value?.let { product ->
                val appetizerClear = quantity < quantityChanged
                quantityChanged = quantity
                addOrUpdateProductToCurrentOrder(quantityChanged, product)
                val subTotal = calculateSubTotalByProduct(quantity, product)
                setupStateButtonSubTotal(
                    subTotal.getAmountFormat(), isQuantityEnabled(quantityChanged)
                )
                handleAppetizerByProductQuantity(appetizerClear)
            }
        }

    private fun handleAppetizerByProductQuantity(shouldClearAppetizer: Boolean) {
        product.value?.let {
            if (!it.isMenuDishType()) return
            if (shouldClearAppetizer)
                clearAppetizers()
            else
                matchAppetizerByOrder()
        }
    }

    private fun clearAppetizers() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            product.value?.let {
                clearAppetizersOrder(it)
                matchAppetizerByOrder()
            }
        }
    }

    private fun setOrderAndVerifyQuantity(orderDetail: OrderDetail) {
        val subTotal = orderDetail.calculateSubTotal().getAmountFormat()
        quantityChanged = orderDetail.quantity
        mQuantity.postValue(orderDetail.quantity)
        setupStateButtonSubTotal(subTotal, enabledButton = true)
    }

    fun addOrUpdateToCurrentOrder() =
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mProduct.value?.let { product ->
                addOrUpdateProductToCurrentOrder(quantityChanged, product)
                mEventProductAddedToCardSuccess.postValue(UIKitEvent(Unit))
                eventBusWrapper.send(AddedProductToCurrentOrderSuccessfullyEvent)
            }
        }

    private fun isQuantityEnabled(quantity: Int): Boolean = quantity > 0
    private fun setupStateButtonSubTotal(subTotal: String, enabledButton: Boolean) {
        when (mStateScreen.value) {
            is StateScreen.Update -> {
                if (quantityChanged == 0)
                    setButtonSubTotalState(
                        ButtonStateModel.getDeleteButtonState()
                    )
                else
                    setButtonSubTotalState(
                        ButtonStateModel.getUpdateButtonState(
                            paramTitle = subTotal,
                            enabledButton
                        )
                    )
            }

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

    private fun fetchAppetizerByCurrentRestaurant() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            appetizers = fetchAppetizerByCurrentRestaurant.invoke()
            matchAppetizerByOrder()
        }
    }

    private fun matchAppetizerByOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            product.value?.let {
                val orderAppetizerList = matchAppetizersByOrder(appetizers, it)
                val appetizerModelList = orderAppetizerList.map { appetizerOrder ->
                    val actionEnable = validateEnableAppetizer(
                        fetchAppetizersAddedCount(orderAppetizerList)
                    )
                    AppetizerStateModel(
                        appetizerOrder,
                        actionEnable.second,
                        actionEnable.first
                    )
                }
                mAppetizersStateModel.postValue(appetizerModelList)
            }
        }
    }

    private fun fetchAppetizersAddedCount(appetizerOrders: List<OrderAppetizerDetail>): Int {
        return appetizerOrders.sumOf { appetizer -> appetizer.quantity }
    }

    fun handleAppetizerQuantity(appetizer: Product, quantity: Int) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            product.value?.let {
                addOrUpdateAppetizerToCurrentOrder(quantity, appetizer, it)
                matchAppetizerByOrder()
            }
        }
    }

    private fun validateEnableAppetizer(appetizerAddedSize: Int): Pair<ButtonEnableType, Boolean> {
        if (quantityChanged == 0) return Pair(ButtonEnableType.ALL, false)
        if (appetizerAddedSize == quantityChanged) {
            return Pair(ButtonEnableType.PLUS, false)
        }
        return Pair(ButtonEnableType.ALL, true)
    }

    fun validateBackAction() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            if (mStateScreen.value == StateScreen.Add) {
                product.value?.let {
                    deleteProductToCurrentOrder(it)
                }
            }
            mEventProductAddedToCardSuccess.postValue(UIKitEvent(Unit))
            eventBusWrapper.send(AddedProductToCurrentOrderSuccessfullyEvent)
        }
    }

    sealed interface StateScreen {
        object Add : StateScreen
        object Update : StateScreen
    }
}