package com.afoxplus.orders.delivery.views.activities

import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityAddProductToOrderBinding
import com.afoxplus.orders.delivery.viewmodels.AddProductToOrderViewModel
import com.afoxplus.orders.delivery.views.fragments.AddProductToCartFragment
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.extensions.parcelable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductToOrderActivity : UIKitBaseActivity() {

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
        binding.marketOrderToolBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        addFragmentToActivity(
            supportFragmentManager,
            AddProductToCartFragment.getInstance(),
            binding.fragmentContainer.id
        )
        setInitialButtonText()
        binding.buttonViewOrder.setOnClickListener { addProductToOrderViewModel.addOrUpdateToCurrentOrder() }
        onBackPressedDispatcher.addCallback {
            this@AddProductToOrderActivity.addProductToOrderViewModel.onBackAction()
        }
    }

    override fun observerViewModel() {
        lifecycleScope.launch {
            addProductToOrderViewModel.events.collectLatest { events ->
                when (events) {
                    is AddProductToOrderViewModel.Events.CloseScreen -> finish()
                }
            }
        }

        addProductToOrderViewModel.buttonSubTotalState.observe(this) { model ->
            binding.buttonViewOrder.isEnabled = model.enabled
            binding.buttonViewOrder.text = getString(model.title, model.paramTitle)
        }
    }

    private fun setInitialButtonText() {
        binding.buttonViewOrder.text = getString(R.string.orders_market_add_product, "")
    }

    private fun getIntentData() {
        intent.parcelable<Product>(Product::class.java.name)?.let { product ->
            addProductToOrderViewModel.startWithProduct(product)
        }
    }

}