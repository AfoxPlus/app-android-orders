package com.afoxplus.orders.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afoxplus.orders.delivery.flow.OrderFlow
import com.afoxplus.orders.demo.databinding.ActivityMainBinding
import com.afoxplus.products.delivery.flow.ProductFlow
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
}
