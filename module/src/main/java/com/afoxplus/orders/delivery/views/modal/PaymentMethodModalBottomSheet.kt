package com.afoxplus.orders.delivery.views.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter: ItemPaymentMethodAdapter = ItemPaymentMethodAdapter(paymentMethodListener)

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
            //layoutManager = LinearLayoutManager(requireContext())
            adapter = adapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
        }
        adapter.submitList(
            mutableListOf(
                PaymentMethod("12", "Efectivo", true),
                PaymentMethod("13", "Yape", false),
                PaymentMethod("14", "Plin", false),
                PaymentMethod("15", "Credit Card", false)
            )
        )
    }

    fun submitList(paymentMethods: List<PaymentMethod>) {

    }

    /* override fun getTheme(): Int {
         return R.style.CustomBottomSheet
     }*/
}