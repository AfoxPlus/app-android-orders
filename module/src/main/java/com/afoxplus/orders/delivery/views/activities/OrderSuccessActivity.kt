package com.afoxplus.orders.delivery.views.activities

import com.afoxplus.orders.databinding.ActivityOrderSuccessBinding
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSuccessActivity : UIKitBaseActivity() {
    private lateinit var binding: ActivityOrderSuccessBinding
    private val fragment = OrderSentSuccessfullyFragment.getInstance()

    override fun setMainView() {
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpView() {
        addFragmentToActivity(
            supportFragmentManager,
            fragment,
            binding.fragmentContainer.id
        )
    }
}