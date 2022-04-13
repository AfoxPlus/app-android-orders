package com.afoxplus.orders.delivery.views.activities

import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrdersMarketPanelBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.viewmodels.MarketOrderViewModel
import com.afoxplus.products.delivery.flow.ProductFlow
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.adapters.ViewPagerAdapter
import com.afoxplus.uikit.bus.EventObserver
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MarketOrderActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersMarketPanelBinding

    @Inject
    lateinit var productFlow: ProductFlow

    @Inject
    lateinit var orderFlow: OrderFlow

    private val marketOrderViewModel: MarketOrderViewModel by viewModels()

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val marketOrderTabs: List<String> by lazy { listOf(*resources.getStringArray(R.array.orders_market_order_tabs)) }
    private val marketOrderTabIcons: List<Int> by lazy {
        listOf(
            R.drawable.orders_ic_carta,
            R.drawable.orders_ic_menu
        )
    }

    override fun setMainView() {
        binding = ActivityOrdersMarketPanelBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun setUpView() {
        binding.viewModel = marketOrderViewModel
        binding.marketOrderToolBar.setNavigationOnClickListener { marketOrderViewModel.onBackPressed() }
        setUpMarkerOrderTab()
        binding.marketOrderRestaurantName.text = "Rest. Doña Esther"
    }

    override fun observerViewModel() {
        marketOrderViewModel.goToAddCardProductEvent.observe(this, EventObserver { product ->
            orderFlow.goToAddProductToOrderActivity(this, product)
        })

        marketOrderViewModel.eventOnClickViewOrder.observe(this, EventObserver { order ->
            orderFlow.goToOrderPreviewActivity(this, order)
        })

        marketOrderViewModel.order.observe(this) { order ->
            order?.let { binding.buttonViewOrder.visibility = View.VISIBLE }
        }

        marketOrderViewModel.eventOnBackPressed.observe(this, EventObserver { onBackPressed() })
    }

    private fun setUpMarkerOrderTab() {
        viewPagerAdapter = ViewPagerAdapter(
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