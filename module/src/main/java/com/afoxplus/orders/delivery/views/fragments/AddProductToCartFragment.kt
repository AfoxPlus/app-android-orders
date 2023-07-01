package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrdersAddProductToCartBinding
import com.afoxplus.orders.delivery.viewmodels.AddProductToOrderViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemAppetizerAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemAppetizerListener
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.fragments.UIKitBaseFragment

class AddProductToCartFragment : UIKitBaseFragment(), ItemAppetizerListener {

    private lateinit var binding: FragmentOrdersAddProductToCartBinding
    private val addProductToOrderViewModel: AddProductToOrderViewModel by activityViewModels()

    private val adapter: ItemAppetizerAdapter by lazy { ItemAppetizerAdapter(this) }

    companion object {
        fun getInstance(): AddProductToCartFragment = AddProductToCartFragment()
    }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersAddProductToCartBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun setUpView() {
        binding.quantityButtonQuantity.onValueChangeListener = {
            addProductToOrderViewModel.calculateSubTotalByProduct(it)
        }
        binding.recyclerProductAppetizer.adapter = adapter
    }

    override fun observerViewModel() {
        addProductToOrderViewModel.quantity.observe(viewLifecycleOwner) {
            binding.quantityButtonQuantity.value = it ?: 0
        }

        addProductToOrderViewModel.appetizerVisibility.observe(viewLifecycleOwner) {
            binding.sectionAppetizer.visibility = it
        }

        addProductToOrderViewModel.appetizersStateModel.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun updateQuantity(product: Product, quantity: Int) {
        addProductToOrderViewModel.handleAppetizerQuantity(product, quantity)
    }
}