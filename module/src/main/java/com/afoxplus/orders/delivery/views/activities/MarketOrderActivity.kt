package com.afoxplus.orders.delivery.views.activities

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrdersMarketPanelBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.viewmodels.MarketOrderViewModel
import com.afoxplus.orders.delivery.views.events.GoToHomeEvent
import com.afoxplus.orders.delivery.views.events.GoToNewOrderEvent
import com.afoxplus.products.delivery.flow.ProductFlow
import com.afoxplus.products.delivery.views.events.OnClickProductSaleEvent
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.adapters.UIKitViewPagerAdapter
import com.afoxplus.uikit.modal.UIKitModal
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarketOrderActivity : UIKitBaseActivity() {

    private lateinit var binding: ActivityOrdersMarketPanelBinding

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, MarketOrderActivity::class.java)
        }
    }

    @Inject
    lateinit var productFlow: ProductFlow

    @Inject
    lateinit var orderFlow: OrderFlow

    private val marketOrderViewModel: MarketOrderViewModel by viewModels()

    private lateinit var viewPagerAdapter: UIKitViewPagerAdapter
    private val marketOrderTabs: List<String> by lazy { listOf(*resources.getStringArray(R.array.orders_market_order_tabs)) }
    private val marketOrderTabIcons: List<Int> by lazy {
        listOf(
            R.drawable.orders_ic_carta,
            R.drawable.orders_ic_menu
        )
    }

    override fun setMainView() {
        binding = ActivityOrdersMarketPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpView() {
        marketOrderViewModel.loadCurrentOrder()
        binding.marketOrderToolBar.setNavigationOnClickListener { marketOrderViewModel.onBackPressed() }
        setUpMarkerOrderTab()
        binding.marketOrderToolBar.subtitle = marketOrderViewModel.restaurantName()
        binding.marketOrderToolBar.title = getString(R.string.order_market_restaurant)
        binding.buttonViewOrder.setOnClickListener {
            marketOrderViewModel.onClickViewOrder()
        }

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    marketOrderViewModel.onBackPressed()
                }
            })
    }

    override fun observerViewModel() {
        lifecycleScope.launchWhenCreated {
            marketOrderViewModel.onEventBusListener.collectLatest { events ->
                if (events is OnClickProductSaleEvent) {
                    orderFlow.goToAddProductToOrderActivity(
                        this@MarketOrderActivity,
                        events.product
                    )
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            marketOrderViewModel.onMarketOrderEvent.collectLatest { events ->
                when (events) {
                    is MarketOrderViewModel.MarketOrderEvent.OnClickViewOrder -> {
                        orderFlow.goToOrderPreviewActivity(this@MarketOrderActivity, events.order)
                    }

                    is MarketOrderViewModel.MarketOrderEvent.OnBackPressed -> {
                        finish()
                    }
                }
            }
        }

        marketOrderViewModel.order.observe(this) { order ->
            order?.let {
                if (order.getOrderDetails().isEmpty())
                    binding.buttonViewOrder.visibility = View.GONE
                else
                    binding.buttonViewOrder.visibility = View.VISIBLE
                binding.buttonViewOrder.text = it.getLabelViewMyOrder()
            }
        }

        marketOrderViewModel.displayOrderModalLiveData.observe(this) {
            displayOrderModal()
        }
    }

    private fun displayOrderModal() {
        UIKitModal.Builder(supportFragmentManager)
            .title(getString(R.string.order_modal_want_to_leave))
            .message(getString(R.string.order_modal_lost_products_message))
            .positiveButton(getString(R.string.order_modal_affirmative_action)) {
                marketOrderViewModel.clearOrderAndGoBack()
                it.dismiss()
            }
            .negativeButton(getString(R.string.order_modal_negative_action)) {
                it.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            marketOrderViewModel.onEventBusListener.collectLatest { events ->
                when (events) {
                    GoToNewOrderEvent ->
                        binding.buttonViewOrder.visibility = View.GONE

                    GoToHomeEvent -> finish()
                }
            }
        }
    }

    private fun setUpMarkerOrderTab() {
        viewPagerAdapter = UIKitViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            listOf(
                productFlow.getProductsSaleFragment(),
                productFlow.getProductMenuFragment()
            )
        )
        binding.viewPagerMarket.adapter = viewPagerAdapter
        binding.viewPagerMarket.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(
            binding.marketOrderProductsTab,
            binding.viewPagerMarket
        ) { tab, position ->
            tab.text = marketOrderTabs[position]
            tab.icon = ContextCompat.getDrawable(this, marketOrderTabIcons[position])
        }.attach()
    }

}