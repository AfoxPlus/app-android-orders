package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentShopCartBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemCartProductAdapter
import com.afoxplus.uikit.fragments.BaseFragment

class ShopCartFragment : BaseFragment() {
    private lateinit var binding: FragmentShopCartBinding
    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()
    private val adapter: ItemCartProductAdapter by lazy { ItemCartProductAdapter() }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentShopCartBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        fun getInstance(): ShopCartFragment = ShopCartFragment()
    }

    override fun setUpView() {
        binding.shopCartViewModel = cartProductsViewModel
        binding.adapter = adapter
        binding.marketName.text = "Restaurante Doña Esther"
    }

    override fun observerViewModel() {
        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            adapter.submitList(order?.getOrderDetails())
        }
    }
}