package com.afoxplus.orders.delivery.views.activities

import androidx.activity.viewModels
import com.afoxplus.orders.databinding.ActivityAddCartProductBinding
import com.afoxplus.orders.delivery.viewmodels.AddCartProductViewModel
import com.afoxplus.orders.delivery.views.fragments.AddCartProductFragment
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.activities.BaseActivity
import com.afoxplus.uikit.activities.extensions.addFragmentToActivity
import com.afoxplus.uikit.bus.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCartProductActivity : BaseActivity() {

    private lateinit var binding: ActivityAddCartProductBinding
    private val addCartProductViewModel: AddCartProductViewModel by viewModels()

    override fun setMainView() {
        binding = ActivityAddCartProductBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun setUpView() {
        getIntentData()
        binding.viewModel = addCartProductViewModel
        addFragmentToActivity(
            supportFragmentManager,
            AddCartProductFragment.getInstance(),
            binding.fragmentContainer.id
        )
    }

    override fun observerViewModel() {
        addCartProductViewModel.eventProductAddedToCardSuccess.observe(this, EventObserver {
            finish()
        })
    }

    private fun getIntentData() {
        intent.getParcelableExtra<Product>(Product::class.java.name)?.let { product ->
            addCartProductViewModel.setProduct(product)
        }
    }

}