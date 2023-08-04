package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.afoxplus.orders.databinding.ActivityOrderSuccessBinding
import com.afoxplus.orders.delivery.viewmodels.OrderSuccessViewModel
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OrderSuccessActivity : UIKitBaseActivity() {
    private val viewModel: OrderSuccessViewModel by viewModels()
    private lateinit var binding: ActivityOrderSuccessBinding
    private val fragment = OrderSentSuccessfullyFragment.getInstance()

    override fun setMainView() {
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observerViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.onEventBusListener.collectLatest { events ->
                when (events) {
                    GoToNewOrderEvent -> finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.clickOnNewOrder()
    }

    override fun setUpView() {
        addFragmentToActivity(
            supportFragmentManager,
            fragment,
            binding.fragmentContainer.id
        )
    }
}