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
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.domain.usecases.AddOrUpdateAppetizerToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.AddOrUpdateProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.CalculateSubTotalByProductUseCase
import com.afoxplus.orders.domain.usecases.ClearAppetizersOrderUseCase
import com.afoxplus.orders.domain.usecases.DeleteProductToCurrentOrderUseCase
import com.afoxplus.orders.domain.usecases.FindProductInOrderUseCase
import com.afoxplus.orders.domain.usecases.MatchAppetizersByOrderUseCase
import com.afoxplus.products.entities.Product
import com.afoxplus.products.usecases.actions.FetchAppetizerByCurrentRestaurant
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.customview.quantitybutton.ButtonEnableType
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddProductToOrderViewModel @Inject constructor(
    private val eventBusWrapper: UIKitEventBusWrapper,
    private val findProductInOrder: FindProductInOrderUseCase,
    private val calculateSubTotalByProduct: CalculateSubTotalByProductUseCase,
    private val addOrUpdateProductToCurrentOrder: AddOrUpdateProductToCurrentOrderUseCase,
    private val deleteProductToCurrentOrder: DeleteProductToCurrentOrderUseCase,
    private val fetchAppetizerByCurrentRestaurant: FetchAppetizerByCurrentRestaurant,
    private val matchAppetizersByOrder: MatchAppetizersByOrderUseCase,
    private val addOrUpdateAppetizerToCurrentOrder: AddOrUpdateAppetizerToCurrentOrderUseCase,
    private val clearAppetizersOrder: ClearAppetizersOrderUseCase,
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
    private val mAppetizersShowModal: MutableLiveData<Unit> by lazy { MutableLiveData() }
    private val mNotesProduct: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val notesProduct: LiveData<String> get() = mNotesProduct

    val product: LiveData<Product> get() = mProduct
    val appetizersStateModel: LiveData<List<AppetizerStateModel>> get() = mAppetizersStateModel
    val appetizersShowModal: LiveData<Unit> get() = mAppetizersShowModal
    val quantity: LiveData<Int> get() = mQuantity
    val buttonSubTotalState: LiveData<ButtonStateModel> get() = mButtonSubTotalState
    val appetizerVisibility: LiveData<Int> get() = mAppetizerVisibility

    private var quantityChanged: Int = 0
    private var notesChanged: String = ""
    private var appetizerAddedQuantity: Int = 0

    private var appetizers: List<Product> = arrayListOf()

    private val mEvents: MutableSharedFlow<Events> by lazy { MutableSharedFlow() }
    val events = mEvents.asSharedFlow()

    fun startWithProduct(product: Product) {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mProduct.postValue(product)
            findProductInOrder(product)?.let { orderDetail ->
                setupUpdateScreen(orderDetail)
            } ?: mQuantity.postValue(null)
            handleProductType(product)
        }
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

    fun calculateSubTotalByProduct(quantity: Int) {
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
    }

    fun noteChangeListener(notes: String) {
        notesChanged = notes
    }

    private fun handleAppetizerByProductQuantity(shouldClearAppetizer: Boolean) {
        product.value?.let {
            if (!it.isMenuDishType()) return
            if (shouldClearAppetizer) {
                clearAppetizers()
                displayAppetizerModal()
            } else
                matchAppetizerByOrder()
        }
    }

    private fun displayAppetizerModal() {
        if (appetizerAddedQuantity > 0)
            mAppetizersShowModal.postValue(Unit)
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
        notesChanged = orderDetail.notes
        mQuantity.postValue(orderDetail.quantity)
        mNotesProduct.postValue(orderDetail.notes)
        setupStateButtonSubTotal(subTotal, enabledButton = true)
    }

    fun addOrUpdateToCurrentOrder() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            mProduct.value?.let { product ->
                addOrUpdateProductToCurrentOrder(
                    quantityChanged,
                    product,
                    notesChanged
                )
                mEvents.emit(Events.CloseScreen)
                eventBusWrapper.send(AddedProductToCurrentOrderSuccessfullyEvent)
            }
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
        appetizerAddedQuantity = appetizerOrders.sumOf { appetizer -> appetizer.quantity }
        return appetizerAddedQuantity
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

    fun onBackAction() {
        viewModelScope.launch(coroutines.getMainDispatcher()) {
            //Added tracking
            if (mStateScreen.value == StateScreen.Add) {
                product.value?.let {
                    deleteProductToCurrentOrder(it)
                }
            }
            mEvents.emit(Events.CloseScreen)
        }
    }

    sealed interface StateScreen {
        object Add : StateScreen
        object Update : StateScreen
    }

    sealed interface Events {
        object CloseScreen : Events

    }
}