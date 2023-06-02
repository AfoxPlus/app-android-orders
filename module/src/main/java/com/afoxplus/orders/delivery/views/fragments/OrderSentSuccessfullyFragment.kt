package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrderSentSuccessfullyBinding
import com.afoxplus.orders.delivery.viewmodels.OrderSuccessViewModel
import com.afoxplus.uikit.fragments.UIKitBaseFragment

class OrderSentSuccessfullyFragment : UIKitBaseFragment() {
    private lateinit var binding: FragmentOrderSentSuccessfullyBinding
    private val viewModel: OrderSuccessViewModel by activityViewModels()

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrderSentSuccessfullyBinding.inflate(inflater)
        binding.buttonViewOrder.setOnClickListener { viewModel.clickOnNewOrder() }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        fun getInstance(): OrderSentSuccessfullyFragment = OrderSentSuccessfullyFragment()
    }

}