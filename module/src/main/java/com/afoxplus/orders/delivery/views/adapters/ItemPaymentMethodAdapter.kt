package com.afoxplus.orders.delivery.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemPaymentMethodListener
import com.afoxplus.orders.delivery.views.adapters.viewholders.ItemPaymentMethodViewHolder
import com.afoxplus.uikit.objects.vendor.PaymentMethod

class ItemPaymentMethodAdapter(private val itemPaymentMethodListener: ItemPaymentMethodListener) :
    RecyclerView.Adapter<ItemPaymentMethodViewHolder>() {
    private var paymentMethods: List<PaymentMethod> = arrayListOf()

    fun submitList(paymentMethods: List<PaymentMethod>) {
        this.paymentMethods = paymentMethods
        notifyDataSetChanged()
        //notifyItemRangeChanged(0, paymentMethods.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPaymentMethodViewHolder =
        ItemPaymentMethodViewHolder.from(parent, itemPaymentMethodListener)

    override fun getItemCount(): Int = paymentMethods.size


    override fun onBindViewHolder(holder: ItemPaymentMethodViewHolder, position: Int) {
        holder.bind(paymentMethods[position])
    }
}

class PaymentMethodsDiffUtil : DiffUtil.ItemCallback<PaymentMethod>() {
    override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean =
        oldItem.id == newItem.id && oldItem.isSelected == newItem.isSelected

}