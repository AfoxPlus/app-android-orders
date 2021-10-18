package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentCartProductsBinding
import com.afoxplus.orders.delivery.viewmodels.CartProductsViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemCartProductAdapter
import com.afoxplus.uikit.fragments.BaseFragment


internal class CartProductsFragment : BaseFragment() {
    private lateinit var binding: FragmentCartProductsBinding
    private val cartProductsViewModel: CartProductsViewModel by activityViewModels()
    private val adapter: ItemCartProductAdapter by lazy { ItemCartProductAdapter() }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentCartProductsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        fun getInstance(): CartProductsFragment = CartProductsFragment()
    }

    override fun setUpView() {
        binding.cartProductsViewModel = cartProductsViewModel
        binding.adapter = adapter
        binding.marketName.text = "Restaurante Doña Esther"
    }

    override fun observerViewModel() {
        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            adapter.submitList(order?.getOrderDetails())
        }
    }
}