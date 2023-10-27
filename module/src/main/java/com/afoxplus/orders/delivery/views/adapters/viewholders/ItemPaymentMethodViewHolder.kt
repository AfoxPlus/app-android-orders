package com.afoxplus.orders.delivery.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.databinding.ItemPaymentMethodBinding
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemPaymentMethodListener
import com.afoxplus.uikit.objects.vendor.PaymentMethod

class ItemPaymentMethodViewHolder(
    private val binding: ItemPaymentMethodBinding,
    private val itemPaymentMethodListener: ItemPaymentMethodListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(paymentMethod: PaymentMethod) {
        binding.modalItemName.text = paymentMethod.name
        binding.modalItemSelected.visibility =
            if (paymentMethod.isSelected) View.VISIBLE else View.GONE
        binding.root.setOnClickListener {
            itemPaymentMethodListener.onSelected(paymentMethod)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemPaymentMethodListener: ItemPaymentMethodListener
        ): ItemPaymentMethodViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPaymentMethodBinding.inflate(layoutInflater, parent, false)
            return ItemPaymentMethodViewHolder(binding, itemPaymentMethodListener)
        }
    }
}
