package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityAddProductToOrderBinding
import com.afoxplus.orders.delivery.viewmodels.AddProductToOrderViewModel
import com.afoxplus.orders.delivery.views.fragments.AddProductToCartFragment
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductToOrderActivity : BaseActivity() {

    private lateinit var binding: ActivityAddProductToOrderBinding
    private val addProductToOrderViewModel: AddProductToOrderViewModel by viewModels()

    override fun setMainView() {
        binding = ActivityAddProductToOrderBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun setUpView() {
        getIntentData()
        binding.viewModel = addProductToOrderViewModel
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }
        addFragmentToActivity(
            supportFragmentManager,
            AddProductToCartFragment.getInstance(),
            binding.fragmentContainer.id
        )
        setInitialButtonText()
    }

    override fun observerViewModel() {
        addProductToOrderViewModel.eventProductAddedToCardSuccess.observe(this, EventObserver {
            finish()
        })

        addProductToOrderViewModel.order.observe(this) {
            it?.let { order ->
                binding.buttonViewOrder.text =
                    getString(R.string.orders_market_add_product, order.getTotalWithFormat())
            } ?: setInitialButtonText()
        }
    }

    private fun setInitialButtonText() {
        binding.buttonViewOrder.text = getString(R.string.orders_market_add_product, "")
    }

    private fun getIntentData() {
        intent.getParcelableExtra<Product>(Product::class.java.name)?.let { product ->
            addProductToOrderViewModel.setProduct(product)
        }
    }

}