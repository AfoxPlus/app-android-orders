package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityOrdersPreviewBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderPreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersPreviewBinding
    private val shopCartViewModel: ShopCartViewModel by viewModels()
    private val shopCartProductFragment: ShopCartFragment by lazy { ShopCartFragment.getInstance() }

    override fun setMainView() {
        binding = ActivityOrdersPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpView() {
        addFragmentToActivity(
            supportFragmentManager,
            shopCartProductFragment,
            binding.fragmentContainer.id
        )
        binding.marketName.text = "Restaurante Doña Esther"
        binding.topAppBar.setNavigationOnClickListener { this.onBackPressed() }
        binding.buttonSendOrder.setOnClickListener {
            shopCartViewModel.onClickSendOrder()
        }
    }

    override fun observerViewModel() {
        shopCartViewModel.eventOnClickSendOrder.observe(this, EventObserver {
            showOrderSentSuccessfullyFragment()
        })

        shopCartViewModel.nameButtonSendOrderLiveData.observe(this) {
            binding.buttonSendOrder.text = it
        }
    }

    private fun showOrderSentSuccessfullyFragment() {
        val fragment = OrderSentSuccessfullyFragment.getInstance()
        supportFragmentManager.beginTransaction().run {
            remove(shopCartProductFragment)
            add(binding.fragmentContainer.id, fragment).commit()
        }
    }

}