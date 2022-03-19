package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityOrdersShopCartBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.orders.entities.Order
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopCartActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersShopCartBinding
    private val shopCartViewModel: ShopCartViewModel by viewModels()
    private val shopCartProductFragment: ShopCartFragment by lazy { ShopCartFragment.getInstance() }

    override fun setMainView() {
        binding = ActivityOrdersShopCartBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun setUpView() {
       getIntentData()
        addFragmentToActivity(
            supportFragmentManager,
            shopCartProductFragment,
            binding.fragmentContainer.id
        )
    }

    override fun observerViewModel() {
        shopCartViewModel.eventOnClickSendOrder.observe(this, EventObserver {
            showOrderSentSuccessfullyFragment()
        })
    }

    private fun getIntentData() {
        intent.getParcelableExtra<Order>(Order::class.java.name)?.let { order ->
            shopCartViewModel.setOrder(order)
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