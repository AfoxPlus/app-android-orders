package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrdersAddProductToCartBinding
import com.afoxplus.orders.delivery.viewmodels.AddProductToOrderViewModel
import com.afoxplus.uikit.fragments.BaseFragment

class AddProductToCartFragment : BaseFragment() {

    private lateinit var binding: FragmentOrdersAddProductToCartBinding
    private val addProductToOrderViewModel: AddProductToOrderViewModel by activityViewModels()

    companion object {
        fun getInstance(): AddProductToCartFragment = AddProductToCartFragment()
    }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersAddProductToCartBinding.inflate(inflater)
        binding.addCartProductViewModel = addProductToOrderViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun observerViewModel() {
        addProductToOrderViewModel.quantity.observe(viewLifecycleOwner) {
            binding.labelQuantity.text = it?.toString() ?: "-"
        }
    }
}