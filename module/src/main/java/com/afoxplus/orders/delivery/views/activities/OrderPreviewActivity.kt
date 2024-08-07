package com.afoxplus.orders.delivery.views.activities

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
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.modal.UIKitModal
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OrderPreviewActivity : UIKitBaseActivity() {

    private lateinit var binding: ActivityOrdersPreviewBinding
    private val shopCartViewModel: ShopCartViewModel by viewModels()

    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            result.contents?.let {
                analyzeScanResponse(it)
            }
        }


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

        shopCartViewModel.eventOpenScan.observe(this, UIKitEventObserver {
            openScan()
        })

        shopCartViewModel.nameButtonSendOrderLiveData.observe(this) {
            binding.buttonSendOrder.text = it
        }

        shopCartViewModel.eventOnBackSendOrder.observe(this) {
            onBackPressedDispatcher.onBackPressed()
        }

        shopCartViewModel.evenOpenAdditionalInfo.observe(this) {
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


    private fun openScan() {
        val options = ScanOptions().apply {
            setPrompt(getString(R.string.order_scan_prompt))
            setCameraId(0)
            setBeepEnabled(false)
            setTorchEnabled(false)
            setBarcodeImageEnabled(false)
            setOrientationLocked(false)
        }
        barcodeLauncher.launch(options)
    }

    private fun analyzeScanResponse(data: String) {
        data.isNotEmpty().let { shopCartViewModel.setOrderTypeFromScan(data) }
    }
}