package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrderSentSuccessfullyBinding
import com.afoxplus.orders.delivery.viewmodels.OrderSuccessViewModel
import com.afoxplus.orders.delivery.views.activities.ORDER_SUCCESS_MESSAGE
import com.afoxplus.uikit.fragments.UIKitBaseFragment

class OrderSentSuccessfullyFragment : UIKitBaseFragment() {
    private lateinit var binding: FragmentOrderSentSuccessfullyBinding
    private val viewModel: OrderSuccessViewModel by activityViewModels()

    companion object {
        fun getInstance(): OrderSentSuccessfullyFragment {
            return OrderSentSuccessfullyFragment()
        }
    }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrderSentSuccessfullyBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title.text = activity?.intent?.getStringExtra(ORDER_SUCCESS_MESSAGE)
        return binding.root
    }

    override fun setUpView() {
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            buttonViewOrder.setOnClickListener { viewModel.clickOnNewOrder() }
            buttonGoToHome.setOnClickListener { viewModel.clickGoToHome() }
        }
    }
}