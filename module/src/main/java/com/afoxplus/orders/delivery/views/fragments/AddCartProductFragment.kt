package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrdersAddCartProductBinding
import com.afoxplus.orders.delivery.viewmodels.AddCartProductViewModel
import com.afoxplus.uikit.fragments.BaseFragment

class AddCartProductFragment : BaseFragment() {

    private lateinit var binding: FragmentOrdersAddCartProductBinding
    private val addCartProductViewModel: AddCartProductViewModel by activityViewModels()

    companion object {
        fun getInstance(): AddCartProductFragment = AddCartProductFragment()
    }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersAddCartProductBinding.inflate(inflater)
        binding.addCartProductViewModel = addCartProductViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun observerViewModel() {
        addCartProductViewModel.quantity.observe(viewLifecycleOwner) {
            binding.labelQuantity.text = it?.toString() ?: "-"
        }
    }
}