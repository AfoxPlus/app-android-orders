package com.afoxplus.orders.delivery.views.activities

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrdersMarketPanelBinding
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.delivery.viewmodels.MarketOrderViewModel
import com.afoxplus.products.delivery.flow.ProductFlow
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.adapters.UIKitViewPagerAdapter
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        binding.marketOrderToolBar.setNavigationOnClickListener { marketOrderViewModel.onBackPressed() }
        setUpMarkerOrderTab()
        //TODO: Get restaurant
        binding.marketOrderRestaurantName.text = "Rest. Doña Esther"
        binding.buttonViewOrder.setOnClickListener {
            marketOrderViewModel.onClickViewOrder()
        }
    }

    override fun observerViewModel() {
        marketOrderViewModel.goToAddCardProductEvent.observe(this, UIKitEventObserver { product ->
            orderFlow.goToAddProductToOrderActivity(this, product)
        })

        marketOrderViewModel.eventOnClickViewOrder.observe(this, UIKitEventObserver { order ->
            orderFlow.goToOrderPreviewActivity(this, order)
        })

        marketOrderViewModel.order.observe(this) { order ->
            order?.let {
                binding.buttonViewOrder.visibility = View.VISIBLE
                binding.buttonViewOrder.text = it.getLabelViewMyOrder()
            }
        }

        marketOrderViewModel.eventOnBackPressed.observe(
            this,
            UIKitEventObserver { onBackPressed() })

        lifecycleScope.launchWhenCreated {
            marketOrderViewModel.eventOnNewOrder.collectLatest {
                binding.buttonViewOrder.visibility = View.GONE
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