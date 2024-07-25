package com.afoxplus.orders.delivery.views.activities

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrdersPreviewBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.models.SendOrderStatusUIModel
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.orders.delivery.views.fragments.AdditionalOrderInfoFragment
import com.afoxplus.orders.cross.exceptions.ApiErrorException
import com.afoxplus.orders.cross.exceptions.OrderBusinessException
import com.afoxplus.orders.delivery.models.RestaurantModel
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.extensions.parcelable
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.modal.UIKitModal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OrderPreviewActivity : UIKitBaseActivity() {

    private lateinit var binding: ActivityOrdersPreviewBinding
    private val shopCartViewModel: ShopCartViewModel by viewModels()

    private val shopCartProductFragment: ShopCartFragment by lazy { ShopCartFragment.getInstance() }
    private val additionalOrderInfoFragment: AdditionalOrderInfoFragment by lazy { AdditionalOrderInfoFragment.getInstance() }

    private lateinit var currentFragment: UIKitBaseFragment

    @Inject
    lateinit var orderFlow: OrderFlow

    override fun setMainView() {
        binding = ActivityOrdersPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpView() {
        changeFragment(shopCartProductFragment)
        shopCartViewModel.loadData()
        binding.topAppBar.subtitle = getRestaurantFromIntent()?.name
        binding.topAppBar.title = getString(R.string.orders_market_label_my_order)
        binding.topAppBar.setNavigationOnClickListener {
            shopCartViewModel.handleBackPressed(
                currentFragment == additionalOrderInfoFragment
            )
        }
        binding.buttonSendOrder.setOnClickListener {
            shopCartViewModel.handleClickSender(currentFragment == shopCartProductFragment)
        }
    }

    override fun observerViewModel() {
        shopCartViewModel.buttonSendLoading.observe(this, UIKitEventObserver {
            binding.buttonSendOrder.isEnabled = it
        })

        shopCartViewModel.nameButtonSendOrderLiveData.observe(this) {
            binding.buttonSendOrder.text = it
        }

        shopCartViewModel.eventOnBackSendOrder.observe(this) {
            this.onBackPressed()
        }

        shopCartViewModel.eventOpenTableOrder.observe(this) {
            changeFragment(additionalOrderInfoFragment)
        }

        shopCartViewModel.eventRemoveTableOrder.observe(this) {
            removeFragments(additionalOrderInfoFragment, shopCartProductFragment)
        }

        shopCartViewModel.eventOpenSuccessOrder.observe(this) {
            handleSendOrderResponse(it)
        }

        shopCartViewModel.goToAddCardProductEvent.observe(this, UIKitEventObserver { product ->
            orderFlow.goToAddProductToOrderActivity(this, product)
        })
    }

    private fun handleSendOrderResponse(result: SendOrderStatusUIModel) {
        when (result) {
            is SendOrderStatusUIModel.Success -> {
                finish()
                orderFlow.goToOrderSuccessActivity(this, result.message)
            }

            is SendOrderStatusUIModel.Error -> {
                displayErrorModal(result.exception)
            }
        }
    }

    private fun displayErrorModal(exception: Exception) {
        when (exception) {
            is ApiErrorException -> {
                UIKitModal.Builder(supportFragmentManager)
                    .title(exception.contentMessage.value)
                    .message(exception.contentMessage.info)
                    .positiveButton(getString(R.string.order_modal_send_retry)) {
                        shopCartViewModel.retrySendOrder()
                        it.dismiss()
                    }
                    .negativeButton(getString(R.string.order_modal_send_cancel)) {
                        shopCartViewModel.changeButtonSendEnable(true)
                        it.dismiss()
                    }
                    .show()
            }

            is OrderBusinessException -> {
                UIKitModal.Builder(supportFragmentManager)
                    .title(exception.contentMessage.value)
                    .message(exception.contentMessage.info)
                    .positiveButton(getString(R.string.order_appetizers_modal_action)) {
                        shopCartViewModel.changeButtonSendEnable(true)
                        it.dismiss()
                    }
                    .show()
            }
        }

    }

    private fun changeFragment(fragment: UIKitBaseFragment) {
        addFragmentToActivity(
            supportFragmentManager,
            fragment,
            binding.fragmentContainer.id
        )
        currentFragment = fragment
    }

    private fun removeFragments(
        fragmentToRemove: UIKitBaseFragment,
        fragmentToReplace: UIKitBaseFragment
    ) {
        supportFragmentManager.beginTransaction().run {
            remove(fragmentToRemove)
            add(binding.fragmentContainer.id, fragmentToReplace).commit()
        }
        currentFragment = fragmentToReplace
    }

    private fun getRestaurantFromIntent(): RestaurantModel? {
        return intent.parcelable<RestaurantModel>(RESTAURANT_PARAM)
    }

    companion object {
        fun newInstance(activity: Activity, restaurant: RestaurantModel): Intent {
            return Intent(activity, OrderPreviewActivity::class.java).apply {
                putExtra(RESTAURANT_PARAM, restaurant)
            }
        }

        private const val RESTAURANT_PARAM = "RESTAURANT_PARAM"
    }
}