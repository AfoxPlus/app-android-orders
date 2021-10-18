package com.afoxplus.orders.delivery.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.databinding.ItemCartProductBinding
import com.afoxplus.orders.entities.OrderDetail

internal class ItemCartProductViewHolder private constructor(
    private val binding: ItemCartProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orderDetail: OrderDetail) {
        binding.orderDetail = orderDetail
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ItemCartProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCartProductBinding.inflate(layoutInflater, parent, false)
            return ItemCartProductViewHolder(binding)
        }
    }

}