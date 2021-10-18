package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afoxplus.orders.databinding.FragmentOrderSentSuccessfullyBinding
import com.afoxplus.uikit.fragments.BaseFragment

internal class OrderSentSuccessfullyFragment : BaseFragment() {
    private lateinit var binding: FragmentOrderSentSuccessfullyBinding

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrderSentSuccessfullyBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        fun getInstance(): OrderSentSuccessfullyFragment = OrderSentSuccessfullyFragment()
    }

}