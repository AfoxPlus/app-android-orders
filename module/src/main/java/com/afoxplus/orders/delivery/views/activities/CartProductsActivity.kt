package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityOrdersCartProductsBinding
import com.afoxplus.orders.delivery.viewmodels.CartProductsViewModel
import com.afoxplus.orders.delivery.views.fragments.CartProductsFragment
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.orders.entities.Order
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver

internal class CartProductsActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersCartProductsBinding
    private val cartProductsViewModel: CartProductsViewModel by viewModels()
    private val cartProductFragment: CartProductsFragment by lazy { CartProductsFragment.getInstance() }

    override fun setMainView() {
        binding = ActivityOrdersCartProductsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun setUpView() {
        getIntentData()
        addFragmentToActivity(
            supportFragmentManager,
            cartProductFragment,
            binding.fragmentContainer.id
        )
    }

    override fun observerViewModel() {
        cartProductsViewModel.eventOnClickSendOrder.observe(this, EventObserver {
            showOrderSentSuccessfullyFragment()
        })
    }

    private fun getIntentData() {
        intent.getParcelableExtra<Order>(Order::class.java.name)?.let { order ->
            cartProductsViewModel.setOrder(order)
        }
    }
    
    private fun showOrderSentSuccessfullyFragment() {
        val fragment = OrderSentSuccessfullyFragment.getInstance()
        supportFragmentManager.beginTransaction().run {
            remove(cartProductFragment)
            add(binding.fragmentContainer.id, fragment).commit()
        }
    }
}