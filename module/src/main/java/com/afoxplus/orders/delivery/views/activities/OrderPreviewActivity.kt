package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrdersPreviewBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.models.SendOrderStatusUIModel
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.orders.delivery.views.fragments.AdditionalOrderInfoFragment
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.modal.UIKitModal
import dagger.hilt.android.AndroidEntryPoint
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
        binding.topAppBar.subtitle = shopCartViewModel.restaurantName()
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
                displayErrorModal(result.title, result.message)
            }
        }
    }

    private fun displayErrorModal(title: String, message: String) {
        UIKitModal.Builder(supportFragmentManager)
            .title(title)
            .message(message)
            .positiveButton(getString(R.string.order_appetizers_modal_action)) {
                shopCartViewModel.changeButtonSendEnable(true)
                it.dismiss()
            }
            .show()
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
}