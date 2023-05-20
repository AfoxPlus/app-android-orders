package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityOrdersPreviewBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.orders.delivery.views.fragments.TableOrderFragment
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver
import com.afoxplus.uikit.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderPreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersPreviewBinding
    private val shopCartViewModel: ShopCartViewModel by viewModels()

    private val shopCartProductFragment: ShopCartFragment by lazy { ShopCartFragment.getInstance() }
    private val tableOrderFragment: TableOrderFragment by lazy { TableOrderFragment.getInstance() }
    private val orderSentSuccessfullyFragment: OrderSentSuccessfullyFragment by lazy { OrderSentSuccessfullyFragment() }

    private lateinit var currentFragment: BaseFragment
    @Inject
    lateinit var orderFlow: OrderFlow

    override fun setMainView() {
        binding = ActivityOrdersPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpView() {
        changeFragment(shopCartProductFragment)
        binding.marketName.text = "Restaurante Doña Esther"
        binding.topAppBar.setNavigationOnClickListener {
            shopCartViewModel.handleBackPressed(
                currentFragment == tableOrderFragment
            )
        }
        binding.buttonSendOrder.setOnClickListener {
            shopCartViewModel.handleClickSender(currentFragment == shopCartProductFragment)
        }
    }

    override fun observerViewModel() {

        shopCartViewModel.nameButtonSendOrderLiveData.observe(this) {
            binding.buttonSendOrder.text = it
        }

        shopCartViewModel.eventOnBackSendOrder.observe(this) {
            this.onBackPressed()
        }

        shopCartViewModel.eventOpenTableOrder.observe(this) {
            changeFragment(tableOrderFragment)
        }

        shopCartViewModel.eventRemoveTableOrder.observe(this) {
            removeFragments(tableOrderFragment, shopCartProductFragment)
        }

        shopCartViewModel.eventOpenSuccessOrder.observe(this) {
            orderFlow.goToOrderSuccessActivity(this)
        }
    }

    private fun changeFragment(fragment: BaseFragment) {
        addFragmentToActivity(
            supportFragmentManager,
            fragment,
            binding.fragmentContainer.id
        )
        currentFragment = fragment
    }

    private fun removeFragments(
        fragmentToRemove: BaseFragment,
        fragmentToReplace: BaseFragment
    ) {
        supportFragmentManager.beginTransaction().run {
            remove(fragmentToRemove)
            add(binding.fragmentContainer.id, fragmentToReplace).commit()
        }
        currentFragment = fragmentToReplace
    }
}