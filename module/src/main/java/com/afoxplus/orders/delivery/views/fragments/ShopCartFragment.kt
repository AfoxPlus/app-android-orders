package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrdersShopCartBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemCartProductAdapter
import com.afoxplus.uikit.fragments.BaseFragment

class ShopCartFragment : BaseFragment() {
    private lateinit var binding: FragmentOrdersShopCartBinding

    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()
    private val adapter: ItemCartProductAdapter by lazy { ItemCartProductAdapter() }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersShopCartBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun getInstance(): ShopCartFragment = ShopCartFragment()
    }

    override fun setUpView() {
        binding.recyclerProductsOrder.adapter = adapter
    }

    override fun observerViewModel() {
        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            adapter.submitList(order?.getOrderDetails())
        }
    }
}