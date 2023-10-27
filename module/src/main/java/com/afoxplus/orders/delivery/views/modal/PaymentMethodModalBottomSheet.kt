package com.afoxplus.orders.delivery.views.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ModalPaymentMethodsBinding
import com.afoxplus.orders.delivery.views.adapters.ItemPaymentMethodAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemPaymentMethodListener
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentMethodModalBottomSheet(
    private val paymentMethodListener: ItemPaymentMethodListener
) : BottomSheetDialogFragment() {
    private lateinit var binding: ModalPaymentMethodsBinding
    private val mAdapter: ItemPaymentMethodAdapter = ItemPaymentMethodAdapter(paymentMethodListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalPaymentMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpView() {
        binding.modalRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
        }
    }

    fun submitList(paymentMethods: List<PaymentMethod>) {
        mAdapter.submitList(paymentMethods)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}