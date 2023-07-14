package com.afoxplus.orders.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.demo.databinding.ActivityMainBinding
import com.afoxplus.products.delivery.flow.ProductFlow
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var productFlow: ProductFlow

    @Inject
    lateinit var orderFlow: OrderFlow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            orderFlow.goToMarketOrderActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            addFragmentToActivity(
                supportFragmentManager,
                orderFlow.getStateOrdersFragment(),
                binding.fcvStatusOrders.id
            )
        }
    }

}
