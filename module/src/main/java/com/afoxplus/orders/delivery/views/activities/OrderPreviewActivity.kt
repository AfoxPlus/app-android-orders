package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityOrdersPreviewBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.fragments.ShopCartFragment
import com.afoxplus.orders.delivery.views.fragments.OrderSentSuccessfullyFragment
import com.afoxplus.orders.entities.Order
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
        getIntentData()
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
        shopCartViewModel.order.observe(this) { order ->
            displayOrder(order)
        }
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

    private fun displayOrder(order: Order) {
        binding.subTotalOrder.text = order.getTotalWithFormat()
    }
}